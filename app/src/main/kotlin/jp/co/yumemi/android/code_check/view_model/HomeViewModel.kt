package jp.co.yumemi.android.code_check.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.model.RepositoryPreviewUiState
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
class HomeViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    // MutableLiveData to hold the list of repositories returned from GitHub API call
    private val _repositoryList = MutableLiveData<List<RepositoryItem>?>(null)

    /**
     * LiveData object representing the list of repositories.
     * Observers can observe this property to get updates on the repository list.
     */
    val repositoryList: LiveData<List<RepositoryItem>?> = _repositoryList

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
    fun getRepositoryList() {
        viewModelScope.launch {
            _repositoryList.value = githubRepository.searchRepositoryList(searchKeyword)
        }
    }

    fun updateSearchKeyword(keyword: String) {
        searchKeyword = keyword
    }

    fun clearSearchKeyword() {
        searchKeyword = ""
    }

    fun setSelectedRepository(repositoryItem: RepositoryItem){
        RepositoryPreviewUiState.setRepository(repositoryItem)
    }

    /**
     * Clears the repository list.
     *
     * This function sets the value of [_repositoryList] to null, effectively clearing the list.
     * Observers will be notified of the change and can update accordingly.
     */
    fun clearRepositoryList() {
        _repositoryList.value = null
    }
}