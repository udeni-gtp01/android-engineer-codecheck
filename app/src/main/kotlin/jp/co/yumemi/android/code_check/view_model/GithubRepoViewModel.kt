package jp.co.yumemi.android.code_check.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.model.ServerResult
import jp.co.yumemi.android.code_check.repository.GithubRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View Model for the GitHub Repository search feature.
 *
 * @property githubRepository Repository responsible for fetching GitHub data.
 */
@HiltViewModel
class GithubRepoViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    // LiveData to observe server results
    private val _serverResult = MutableLiveData<ServerResult<GitHubResponse>>()
    val serverResult: LiveData<ServerResult<GitHubResponse>> = _serverResult

    // List of GitHub repository items
    private var repositoryList: List<RepositoryItem> = emptyList()

    // LiveData for observing the currently selected repository item
    private val _repositoryItem = MutableLiveData<RepositoryItem>(null)
    val repositoryItem: LiveData<RepositoryItem> = _repositoryItem

    // Mutable state for the search keyword entered by the user
    var searchKeyword by mutableStateOf("")

    /**
     * Search for GitHub repositories based on the current search keyword.
     */
    fun searchRepositoryList() {
        if (searchKeyword.isBlank()) {
            setServerResult(ServerResult.Success(GitHubResponse(emptyList())))
        } else {
            setServerResult(ServerResult.Loading)
            viewModelScope.launch {
                val serverResult: ServerResult<GitHubResponse> =
                    githubRepository.searchRepositoryList(searchKeyword)
                setServerResult(serverResult)
            }
        }
    }

    /**
     * Set the server result and update the list of repository items based on the result.
     *
     * @param serverResult The result of the GitHub repository search.
     */
    private fun setServerResult(serverResult: ServerResult<GitHubResponse>) {
        _serverResult.value = serverResult
        repositoryList = when (serverResult) {
            is ServerResult.Success -> {
                serverResult.data.items
            }

            else -> {
                emptyList()
            }
        }
    }

    /**
     * Get the list of GitHub repository items.
     *
     * @return List of repository items.
     */
    fun getRepositoryList(): List<RepositoryItem> {
        return repositoryList
    }

    /**
     * Set the currently selected repository item.
     *
     * @param selectedRepository The selected GitHub repository.
     */
    fun setRepository(selectedRepository: RepositoryItem) {
        _repositoryItem.value = selectedRepository
    }

    /**
     * Update the search keyword with the given value.
     *
     * @param keyword The search keyword entered by the user.
     */
    fun updateSearchKeyword(keyword: String) {
        searchKeyword = keyword
    }

    /**
     * Clear the search keyword.
     */
    fun clearSearchKeyword() {
        searchKeyword = ""
    }
}