package jp.co.yumemi.android.code_check.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.code_check.model.RepositoryItem
import kotlinx.coroutines.launch


class RepositoryViewModel() : ViewModel() {
    private val _repository= MutableLiveData<RepositoryItem>(null)
    val repository:LiveData<RepositoryItem>
        get() = _repository

    fun setRepository(repository:RepositoryItem){
        _repository.value=repository
        repository.stargazersCount
    }

    fun getRepositorySummary(){
        viewModelScope.launch {
            val repositorySummary=
        }
    }

}