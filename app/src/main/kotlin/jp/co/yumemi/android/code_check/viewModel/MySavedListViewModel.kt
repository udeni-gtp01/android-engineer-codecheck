package jp.co.yumemi.android.code_check.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.constant.ResponseCode
import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.SavedGitHubRepository
import jp.co.yumemi.android.code_check.model.toLocalGitHubRepository
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for managing the UI state and interactions related to the user's saved GitHub repositories list.
 *
 * @param localGitHubDatabaseRepository The repository for accessing local database operations related to GitHub repositories.
 * @param logger The logger for logging errors.
 */
@HiltViewModel
class MySavedListViewModel @Inject constructor(
    private val localGitHubDatabaseRepository: LocalGitHubDatabaseRepository,
    private val logger: Logger
) : ViewModel() {
    // Logging tag for this class
    private val TAG = this.javaClass.simpleName

    /*
    StateFlow for holding the outcome of retrieving the user's saved list.
    It emits [GitHubResponse] objects representing success, error, or loading state.
     */
    private val _mySavedListState =
        MutableStateFlow<GitHubResponse<List<SavedGitHubRepository>>>(GitHubResponse.Success((emptyList())))
    val mySavedListState: StateFlow<GitHubResponse<List<SavedGitHubRepository>>> = _mySavedListState

    /*
    StateFlow for holding the outcome of saving a selected GitHub repository in the database.
    It emits [GitHubResponse] objects representing success, error, or loading state.
     */
    private val _isSelectedGitHubRepositorySavedState =
        MutableStateFlow<GitHubResponse<Boolean>>(GitHubResponse.Loading)
    val isSelectedGitHubRepositorySavedState: StateFlow<GitHubResponse<Boolean>> =
        _isSelectedGitHubRepositorySavedState

    /**
     * Retrieves the user's saved GitHub repositories list from the local database.
     * Updates the UI with the fetched list.
     */
    fun getMySavedList() {
        _mySavedListState.value = GitHubResponse.Loading
        viewModelScope.launch {
            try {
                localGitHubDatabaseRepository.getMySavedList()
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _mySavedListState.value = it
                    }
            } catch (ex: Exception) {
                _mySavedListState.value = GitHubResponse.Error(ResponseCode.EXCEPTION)
                logger.error(
                    TAG,
                    ex.message
                        ?: "An error occurred while retrieving user's saved list",
                    ex
                )
            }
        }
    }

    /**
     * Adds the selected GitHub repository to the user's saved list in the local database.
     *
     * @param savedGitHubRepository The repository to be added to user's saved list.
     */
    fun saveSelectedGitHubRepositoryInDatabase(savedGitHubRepository: SavedGitHubRepository) {
        _isSelectedGitHubRepositorySavedState.value = GitHubResponse.Loading
        viewModelScope.launch {
            try {
                val localGitHubRepository = savedGitHubRepository.toLocalGitHubRepository()
                localGitHubDatabaseRepository.saveSelectedGitHubRepositoryInDatabase(
                    localGitHubRepository
                ).flowOn(Dispatchers.IO).collect {
                    _isSelectedGitHubRepositorySavedState.value = it
                }
            } catch (ex: Exception) {
                _isSelectedGitHubRepositorySavedState.value =
                    GitHubResponse.Error(ResponseCode.EXCEPTION)
                logger.error(
                    TAG,
                    ex.message
                        ?: "An error occurred while saving selected GitHub repository: ${savedGitHubRepository.id}",
                    ex
                )
            }
        }
    }
}