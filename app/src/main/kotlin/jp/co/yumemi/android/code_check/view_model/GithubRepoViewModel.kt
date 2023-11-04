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

    private var repositoryList: List<RepositoryItem> = emptyList()

    // MutableLiveData to hold the currently selected repository
    private val _repositoryItem = MutableLiveData<RepositoryItem>(null)

    /**
     * LiveData object representing repository.
     * This property exposes the repository data to observers
     */
    val repositoryItem: LiveData<RepositoryItem> = _repositoryItem

    private val _serverResult = MutableLiveData<ServerResult<GitHubResponse>>()
    val serverResult: LiveData<ServerResult<GitHubResponse>> = _serverResult

    /**
     * Sets the selected repository.
     * @param selectedRepository The repository to be set.
     */
    fun setRepository(selectedRepository: RepositoryItem) {
        _repositoryItem.value = selectedRepository
    }

    var searchKeyword by mutableStateOf("")

    /**
     * Fetches the list of repositories based on the provided input text.
     *
     * This function launches a coroutine in the viewModelScope to fetch the repository list
     * asynchronously from the [GithubRepository]. The fetched list is then stored in the
     * [_repositoryList] MutableLiveData, which triggers observers to update.
     *
     * @param inputText The text to search for repositories.
     */
    fun searchRepositoryList() {
        _serverResult.value = ServerResult.Loading
        viewModelScope.launch {
            val serverResult: ServerResult<GitHubResponse> =
                githubRepository.searchRepositoryList(searchKeyword)
            _serverResult.value = serverResult
            when (serverResult) {
                is ServerResult.Success -> {
                    repositoryList = serverResult.data.items
                }

                else -> {
                    repositoryList = emptyList()
                }
            }
        }
    }

    fun getRepositoryList(): List<RepositoryItem> {
        return repositoryList
    }

    fun updateSearchKeyword(keyword: String) {
        searchKeyword = keyword
    }

    fun clearSearchKeyword() {
        searchKeyword = ""
    }

    fun setSelectedRepositoryItem(selectedRepositoryItem: RepositoryItem) {
//        RepositoryPreviewUiState.setRepository(repositoryItem)
        _repositoryItem.value = selectedRepositoryItem

    }


    /**
     * Clears the repository list.
     *
     * This function sets the value of [_repositoryList] to null, effectively clearing the list.
     * Observers will be notified of the change and can update accordingly.
     */
    fun clearRepositoryList() {
        repositoryList = emptyList()
    }
}