package jp.co.yumemi.android.code_check.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.co.yumemi.android.code_check.model.RepositoryItem

/**
 * ViewModel class for managing repository data.
 */
class PreviewViewModel : ViewModel() {

    // MutableLiveData to hold the currently selected repository
    private val _repository = MutableLiveData<RepositoryItem>(null)

    /**
     * LiveData object representing repository.
     * This property exposes the repository data to observers
     */
    val repository: LiveData<RepositoryItem>
        get() = _repository

    /**
     * Sets the selected repository.
     * @param repository The repository to be set.
     */
    fun setRepository(repository: RepositoryItem) {
        _repository.value = repository
    }
}