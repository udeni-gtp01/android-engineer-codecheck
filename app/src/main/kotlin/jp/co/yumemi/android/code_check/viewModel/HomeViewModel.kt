package jp.co.yumemi.android.code_check.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.constant.ResponseCode.EXCEPTION
import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.model.GitHubRepository
import jp.co.yumemi.android.code_check.model.GitHubRepositoryList
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.repository.GitHubApiRepository
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for the Home screen shared among Composable functions of HomeScreen and GitHubRepositoryInfoScreen.
 * Handles interactions between UI and data related to GitHub repositories.
 *
 * @param gitHubApiRepository The repository for interacting with the GitHub API.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gitHubApiRepository: GitHubApiRepository,
    private val localGitHubDatabaseRepository: LocalGitHubDatabaseRepository,
    private val logger: Logger
) : ViewModel() {
    // Logging tag for this class
    private val TAG = this.javaClass.simpleName

    // StateFlow for holding the outcome of GitHub repository searches.
    // It emits [GitHubResponse] objects representing success, error, or loading state.
    private val _gitHubSearchResultState = MutableStateFlow<GitHubResponse<GitHubRepositoryList>>(
        GitHubResponse.Success(
            GitHubRepositoryList(
                emptyList()
            )
        )
    )
    val gitHubSearchResultState: StateFlow<GitHubResponse<GitHubRepositoryList>> =
        _gitHubSearchResultState

    private val _isSelectedGitHubRepositorySavedState =
        MutableStateFlow<GitHubResponse<Boolean>>(GitHubResponse.Loading)
    val isSelectedGitHubRepositorySavedState: StateFlow<GitHubResponse<Boolean>> =
        _isSelectedGitHubRepositorySavedState

    //  Two-way data binding property for the user's search keyword.
    var searchKeyword by mutableStateOf("")

    /**
     * Initiates a search for GitHub repositories based on the current value of [searchKeyword].
     *  - If the keyword is empty, a successful response with an empty list is emitted.
     *  - Otherwise, it fetches repositories using the injected [gitHubApiRepository] and updates the UI accordingly.
     */
    fun searchGitHubRepositories() {
        _gitHubSearchResultState.value = GitHubResponse.Loading
        if (searchKeyword.isBlank()) {
            _gitHubSearchResultState.value =
                GitHubResponse.Success(GitHubRepositoryList(emptyList()))
        } else {
            viewModelScope.launch {
                try {
                    gitHubApiRepository.searchGitHubRepositories(searchKeyword)
                        .flowOn(Dispatchers.IO)
                        .collect {
                            _gitHubSearchResultState.value = it
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
     */
    fun clearSearchKeyword() {
        searchKeyword = ""
    }

    fun saveSelectedGitHubRepositoryInDatabase(gitHubRepository: GitHubRepository) {
        _isSelectedGitHubRepositorySavedState.value = GitHubResponse.Loading
        viewModelScope.launch {
            try {
                val localGitHubRepository = LocalGitHubRepository(
                    forksCount = gitHubRepository.forksCount,
                    language = gitHubRepository.language,
                    name = gitHubRepository.name,
                    openIssuesCount = gitHubRepository.openIssuesCount,
                    stargazersCount = gitHubRepository.stargazersCount,
                    watchersCount = gitHubRepository.watchersCount,
                    htmlUrl = gitHubRepository.htmlUrl,
                    ownerLogin = gitHubRepository.owner?.login,
                    ownerAvatarUrl = gitHubRepository.owner?.avatarUrl
                )
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
                        ?: "An error occurred while saving selected GitHub repository: ${gitHubRepository.id}",
                    ex
                )
            }
        }
    }
}