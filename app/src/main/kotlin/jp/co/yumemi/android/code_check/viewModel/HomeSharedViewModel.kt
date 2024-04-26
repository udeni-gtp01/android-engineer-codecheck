package jp.co.yumemi.android.code_check.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.constant.ApiResponseCode.EXCEPTION
import jp.co.yumemi.android.code_check.model.GitHubRepository
import jp.co.yumemi.android.code_check.model.GitHubRepositoryList
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.repository.GitHubApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for the Home screen shared among Composable functions of HomeScreen and PreviewScreen.
 * Handles interactions between UI and data related to GitHub repositories.
 *
 * @param gitHubApiRepository The repository for interacting with the GitHub API.
 */
@HiltViewModel
class HomeSharedViewModel @Inject constructor(
    private val gitHubApiRepository: GitHubApiRepository
) : ViewModel() {
    // Logging tag for this class
    private val TAG = this.javaClass.simpleName

    // LiveData to observe the outcome of GitHub repository searches.
    // It emits [GitHubResponse] objects representing success, error, or loading state.
    private val _gitHubApiResult = MutableLiveData<GitHubResponse<GitHubRepositoryList>>()
    val gitHubApiResult: LiveData<GitHubResponse<GitHubRepositoryList>> = _gitHubApiResult

    // Internal list to store the fetched GitHub repository items.
    private var gitHubRepositoryList: List<GitHubRepository> = emptyList()

    // LiveData to observe the currently selected GitHub repository item.
    private val _gitHubRepository = MutableLiveData<GitHubRepository>(null)
    val gitHubRepository: LiveData<GitHubRepository> = _gitHubRepository

    //  Two-way data binding property for the user's search keyword.
    var searchKeyword by mutableStateOf("")

    /**
     * Initiates a search for GitHub repositories based on the current value of [searchKeyword].
     *  - If the keyword is empty, a successful response with an empty list is emitted.
     *  - Otherwise, it fetches repositories using the injected [gitHubApiRepository] and updates the UI accordingly.
     */
    fun searchGitHubRepositories() {
        if (searchKeyword.isBlank()) {
            setServerResult(GitHubResponse.Success(GitHubRepositoryList(emptyList())))
        } else {
            viewModelScope.launch {
                try {
                    gitHubApiRepository.searchGitHubRepositories(searchKeyword)
                        .flowOn(Dispatchers.IO)
                        .collect {
                            setServerResult(it)
                        }
                } catch (ex: Exception) {
                    setServerResult(GitHubResponse.Error(EXCEPTION))
                    Log.e(
                        TAG,
                        ex.message
                            ?: "An error occurred while searching GitHub repositories for keyword $searchKeyword",
                        ex
                    )
                }
            }
        }
    }

    /**
     * Updates the [_gitHubApiResult] LiveData and the [gitHubRepositoryList] based on the provided [GitHubResponse].
     * This function is responsible for handling different response types (Success, Error, etc.) and updating the UI.
     *
     * @param gitHubResponse The result of the GitHub repository search.
     */
    private fun setServerResult(gitHubResponse: GitHubResponse<GitHubRepositoryList>) {
        _gitHubApiResult.value = gitHubResponse
        gitHubRepositoryList = when (gitHubResponse) {
            is GitHubResponse.Success -> {
                gitHubResponse.data.items
            }

            else -> {
                emptyList()
            }
        }
    }

    /**
     * Returns the current list of fetched GitHub repository items.
     */
    fun getRepositoryList(): List<GitHubRepository> {
        return gitHubRepositoryList
    }

    /**
     * Sets the currently selected GitHub repository item in the [_gitHubRepository] LiveData.
     *
     * @param selectedGitHubRepository The selected GitHub repository.
     */
    fun setRepository(selectedGitHubRepository: GitHubRepository) {
        _gitHubRepository.value = selectedGitHubRepository
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
}