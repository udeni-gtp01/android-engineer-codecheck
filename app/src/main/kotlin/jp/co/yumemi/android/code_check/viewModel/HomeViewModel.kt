package jp.co.yumemi.android.code_check.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.constant.ResponseCode.EXCEPTION
import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.model.toLocalGitHubRepository
import jp.co.yumemi.android.code_check.repository.GitHubApiRepository
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for managing the Home screen's UI state and interactions related to GitHub repositories.
 * This ViewModel is shared among Composable functions of HomeScreen.
 *
 * @param gitHubApiRepository The repository for interacting with the GitHub API.
 * @param localGitHubDatabaseRepository The repository for accessing local database operations related to GitHub repositories.
 * @param logger The logger for logging errors.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gitHubApiRepository: GitHubApiRepository,
    private val localGitHubDatabaseRepository: LocalGitHubDatabaseRepository,
    private val logger: Logger
) : ViewModel() {
    // Logging tag for this class
    private val TAG = this.javaClass.simpleName

    /*
    StateFlow for holding the outcome of GitHub repository searches.
    It emits [GitHubResponse] objects representing success, error, or loading state.
     */
    private val _gitHubSearchResultState =
        MutableStateFlow<GitHubResponse<List<LocalGitHubRepository>>>(
            GitHubResponse.Success(emptyList())
        )
    val gitHubSearchResultState: StateFlow<GitHubResponse<List<LocalGitHubRepository>>> =
        _gitHubSearchResultState

    /*
    StateFlow for holding the outcome of saving the selected GitHub repository in the database.
    It emits [GitHubResponse] objects representing success, error, or loading state.
    This saved GitHub repository data will be shown in `GitHubRepositoryInfoScreen`.
     */
    private val _isSelectedGitHubRepositorySavedState =
        MutableStateFlow<GitHubResponse<Boolean>>(GitHubResponse.Loading)
    val isSelectedGitHubRepositorySavedState: StateFlow<GitHubResponse<Boolean>> =
        _isSelectedGitHubRepositorySavedState

    // Holds user's search keyword.
    var searchKeyword by mutableStateOf("")

    /**
     * Initiates a search for GitHub repositories based on the current value of [searchKeyword].
     * - If the keyword is empty, a successful response with an empty list is emitted.
     * - Otherwise, it fetches repositories using the injected [gitHubApiRepository] and updates the UI accordingly.
     */
    fun searchGitHubRepositories() {
        _gitHubSearchResultState.value = GitHubResponse.Loading
        if (searchKeyword.isBlank()) {
            _gitHubSearchResultState.value =
                GitHubResponse.Success(emptyList())
        } else {
            viewModelScope.launch {
                try {
                    gitHubApiRepository.searchGitHubRepositories(searchKeyword)
                        .flowOn(Dispatchers.IO)
                        .collect { gitHubResponse ->
                            when (gitHubResponse) {
                                is GitHubResponse.Loading -> {
                                    _gitHubSearchResultState.value = gitHubResponse
                                }

                                is GitHubResponse.Success -> {
                                    val searchedRepos = gitHubResponse.data.items
                                    // Fetch the user's saved GitHub repositories from the local database
                                    localGitHubDatabaseRepository.getMySavedList()
                                        .flowOn(Dispatchers.IO)
                                        .collect { databaseResponse ->
                                            if (databaseResponse is GitHubResponse.Success) {
                                                val savedRepos = databaseResponse.data
                                                // Map each searched GitHub repository to update the isSaved property
                                                val updatedRepos =
                                                    searchedRepos.map { searchedRepo ->
                                                        val isSaved =
                                                            savedRepos.any { savedRepo -> savedRepo.id == searchedRepo.id }
                                                        // Create a copy of the GitHub searched repository with the updated isSaved property
                                                        searchedRepo.toLocalGitHubRepository(isSaved = isSaved)
                                                    }
                                                // Update the state with the updated list of repositories
                                                _gitHubSearchResultState.value =
                                                    GitHubResponse.Success(updatedRepos)
                                            }
                                        }
                                }

                                is GitHubResponse.Error -> {
                                    _gitHubSearchResultState.value = gitHubResponse
                                }
                            }
                        }
                } catch (ex: Exception) {
                    _gitHubSearchResultState.value = GitHubResponse.Error(EXCEPTION)
                    logger.error(
                        TAG,
                        ex.message
                            ?: "An error occurred while searching GitHub repositories for keyword: $searchKeyword",
                        ex
                    )
                }
            }
        }
    }

    /**
     * Updates the [searchKeyword] property with the provided new value.
     *
     * @param keyword The search keyword entered by the user.
     */
    fun updateSearchKeyword(keyword: String) {
        searchKeyword = keyword
    }

    /**
     * Clears the [searchKeyword] property by setting it to an empty string.
     * Sets [gitHubSearchResultState] with a successful response of an empty list.
     */
    fun clearSearchKeyword() {
        searchKeyword = ""
        _gitHubSearchResultState.value = GitHubResponse.Success(emptyList())
    }

    /**
     * Saves the selected GitHub repository in the local database
     * to achieve a single source of truth (SSOT) for the selected GitHub repository using Room.
     * This saved GitHub repository represents the most recently selected item to show in `GitHubRepositoryInfoScreen`.
     *
     * @param localGitHubRepository The GitHub repository to be saved in the database.
     */
    fun saveSelectedGitHubRepositoryInDatabase(localGitHubRepository: LocalGitHubRepository) {
        _isSelectedGitHubRepositorySavedState.value = GitHubResponse.Loading
        viewModelScope.launch {
            try {
                localGitHubDatabaseRepository.saveSelectedGitHubRepositoryInDatabase(
                    localGitHubRepository
                ).flowOn(Dispatchers.IO).collect {
                    _isSelectedGitHubRepositorySavedState.value = it
                }
            } catch (ex: Exception) {
                _isSelectedGitHubRepositorySavedState.value = GitHubResponse.Error(EXCEPTION)
                logger.error(
                    TAG,
                    ex.message
                        ?: "An error occurred while saving selected GitHub repository: ${localGitHubRepository.id}",
                    ex
                )
            }
        }
    }

    /**
     * Retrieves the recent updates from the database and updates the UI accordingly.
     * Recent updates includes whether user has added/removed a GitHub repository from user's saved list.
     */
    fun getRecentUpdateFromDatabase() {
        viewModelScope.launch {
            try {
                if (_gitHubSearchResultState.value is GitHubResponse.Success) {
                    // Extract the list of searched GitHub repositories from the current state
                    val gitHubSearchResult =
                        _gitHubSearchResultState.value as GitHubResponse.Success
                    val searchedRepos = gitHubSearchResult.data
                    if (searchedRepos.isNotEmpty()) {
                        // Fetch the user's saved GitHub repositories from the local database
                        localGitHubDatabaseRepository.getMySavedList()
                            .flowOn(Dispatchers.IO)
                            .collect { databaseResponse ->
                                when (databaseResponse) {
                                    is GitHubResponse.Loading -> {
                                        _gitHubSearchResultState.value = databaseResponse
                                    }

                                    is GitHubResponse.Success -> {
                                        val savedRepos = databaseResponse.data
                                        // Map each searched GitHub repository to update the isSaved property
                                        val updatedRepos = searchedRepos.map { searchedRepo ->
                                            val isSaved =
                                                savedRepos.any { savedRepo -> savedRepo.id == searchedRepo.id }
                                            searchedRepo.copy(isSaved = isSaved)
                                        }
                                        // Update the state with the updated list of GitHub repositories
                                        _gitHubSearchResultState.value =
                                            GitHubResponse.Success(updatedRepos)
                                    }

                                    is GitHubResponse.Error -> {
                                        _gitHubSearchResultState.value = databaseResponse
                                    }
                                }
                            }
                    }
                }
            } catch (ex: Exception) {
                _isSelectedGitHubRepositorySavedState.value = GitHubResponse.Error(EXCEPTION)
                logger.error(
                    TAG,
                    ex.message ?: "An error occurred while updating GitHub repository list",
                    ex
                )
            }
        }
    }
}