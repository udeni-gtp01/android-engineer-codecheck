package jp.co.yumemi.android.code_check.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.model.RepositoryPreviewUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel class for managing repository data.
 */
class PreviewViewModel : ViewModel() {
    val repository: LiveData<RepositoryItem> = RepositoryPreviewUiState.repository
}