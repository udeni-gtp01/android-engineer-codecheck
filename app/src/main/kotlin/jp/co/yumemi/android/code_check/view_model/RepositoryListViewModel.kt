package jp.co.yumemi.android.code_check.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.repository.GithubRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryListViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    private val _repositoryList = MutableLiveData<List<RepositoryItem>?>(null)
    val repositoryList: LiveData<List<RepositoryItem>?>
        get() = _repositoryList

    fun getRepositoryList(inputText: String) {
        viewModelScope.launch {
            _repositoryList.value = githubRepository.searchRepositoryList(inputText)
        }
    }

    fun clearRepositoryList() {
        _repositoryList.value = null
    }
}