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
 * ViewModel class for the repository list screen.
 *
 * This class handles the logic and data for displaying a list of repositories.
 * It communicates with the [GithubRepository] to fetch the repository data.
 *
 * @property githubRepository The repository for fetching data from the GitHub API.
 */
@HiltViewModel
class GithubRepoViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {
    private val _serverResult = MutableLiveData<ServerResult<GitHubResponse>>()
    val serverResult: LiveData<ServerResult<GitHubResponse>> = _serverResult

    private var repositoryList: List<RepositoryItem> = emptyList()

    // MutableLiveData to hold the currently selected repository
    private val _repositoryItem = MutableLiveData<RepositoryItem>(null)
    val repositoryItem: LiveData<RepositoryItem> = _repositoryItem

    var searchKeyword by mutableStateOf("")


    /**
     * Fetches the list of repositories based on the provided input text.
     *
     * This function launches a coroutine in the viewModelScope to fetch the repository list
     * asynchronously from the [GithubRepository]. The fetched list is then stored in the
     * [repositoryList] MutableLiveData, which triggers observers to update.
     *
     */
    fun searchRepositoryList() {
        if (searchKeyword.isBlank()) {
            ServerResult.Success(GitHubResponse(emptyList()))
            repositoryList = emptyList()
        } else {
            setServerResult(ServerResult.Loading)
            viewModelScope.launch {
                val serverResult: ServerResult<GitHubResponse> =
                    githubRepository.searchRepositoryList(searchKeyword)
                setServerResult(serverResult)
            }
        }
    }

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

    fun getRepositoryList(): List<RepositoryItem> {
        return repositoryList
    }

    /**
     * Sets the selected repository.
     * @param selectedRepository The repository to be set.
     */
    fun setRepository(selectedRepository: RepositoryItem) {
        _repositoryItem.value = selectedRepository
    }

    fun updateSearchKeyword(keyword: String) {
        searchKeyword = keyword
    }

    fun clearSearchKeyword() {
        searchKeyword = ""
    }
}