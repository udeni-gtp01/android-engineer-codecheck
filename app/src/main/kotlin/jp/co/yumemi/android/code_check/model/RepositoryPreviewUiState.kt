package jp.co.yumemi.android.code_check.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object RepositoryPreviewUiState {
    // MutableLiveData to hold the currently selected repository
    private val _repository = MutableLiveData<RepositoryItem>(null)

    /**
     * LiveData object representing repository.
     * This property exposes the repository data to observers
     */
    val repository: LiveData<RepositoryItem> = _repository

    /**
     * Sets the selected repository.
     * @param repository The repository to be set.
     */
    fun setRepository(repository: RepositoryItem) {
        _repository.value = repository
    }
}
