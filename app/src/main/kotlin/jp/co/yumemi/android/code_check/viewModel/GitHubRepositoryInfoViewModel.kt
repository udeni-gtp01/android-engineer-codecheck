package jp.co.yumemi.android.code_check.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.constant.ResponseCode
import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.model.toSavedGitHubRepository
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class responsible for fetching and exposing information about a selected GitHub repository.
 * This ViewModel retrieves the selected GitHub repository from the [LocalGitHubRepository] table in Room database.
 * The [LocalGitHubRepository] table contains only one row to store the most recently selected
 * github repository by the user to see information and has fixed value (`1`) as the primary key.
 *
 * The retrieved information is exposed as a Flow of `GitHubResponse`, which can represent
 * either success with the repository details or an error if the repository is not found
 * or if an exception occurs during the retrieval process or null if the retrieved argument is null.
 *
 * @param localGitHubDatabaseRepository Repository providing access to local GitHub data.
 * @param logger The logger for logging errors.
 */
@HiltViewModel
class GitHubRepositoryInfoViewModel @Inject constructor(
    private val localGitHubDatabaseRepository: LocalGitHubDatabaseRepository,
    private val logger: Logger
) : ViewModel() {
    // Logging tag for this class
    private val TAG = this.javaClass.simpleName

    /*
    Retrieves information about the selected GitHub repository as a Flow of [GitHubResponse].
    This Flow emits a [GitHubResponse] containing either:
     - Success: The fetched [LocalGitHubRepository] data.
     - Error: Information about an encountered error during data retrieval.
     */
    val gitHubRepositoryInfo: Flow<GitHubResponse<LocalGitHubRepository?>> =
        localGitHubDatabaseRepository.getSelectedGitHubRepositoryFromDatabase()

    /**
     * Adds the specified [localGitHubRepository] to the user's saved list in the local database.
     *
     * @param localGitHubRepository The GitHub repository to be added to the saved list.
     * @param onSuccess Callback invoked when the GitHub repository is successfully added.
     * @param onError Callback invoked if an error occurs during the operation. It receives an error message as a parameter.
     * @param onLoading Callback invoked when the operation is in progress.
     */
    fun addGitHubRepositoryToMySavedList(
        localGitHubRepository: LocalGitHubRepository,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit,
        onLoading: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val savedGitHubRepository =
                    localGitHubRepository.toSavedGitHubRepository(isSaved = true)

                localGitHubDatabaseRepository.addGitHubRepositoryToMySavedList(savedGitHubRepository = savedGitHubRepository)
                    .flowOn(Dispatchers.IO)
                    .collect { addedResult ->
                        when (addedResult) {
                            is GitHubResponse.Success -> onSuccess()
                            is GitHubResponse.Error -> onError(addedResult.error)
                            is GitHubResponse.Loading -> onLoading()
                        }
                    }
            } catch (ex: Exception) {
                onError(ResponseCode.EXCEPTION)
                logger.error(
                    TAG,
                    ex.message
                        ?: "An error occurred while adding GitHub repository to user's saved list: ${localGitHubRepository.id}",
                    ex
                )
            }
        }
    }

    /**
     * Removes the specified [localGitHubRepository] from the user's saved list in the local database.
     *
     * @param localGitHubRepository The GitHub repository to be removed from the saved list.
     * @param onSuccess Callback invoked when the GitHub repository is successfully removed.
     * @param onError Callback invoked if an error occurs during the operation. It receives an error message as a parameter.
     * @param onLoading Callback invoked when the operation is in progress.
     */
    fun removeGitHubRepositoryFromMySavedList(
        localGitHubRepository: LocalGitHubRepository,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit,
        onLoading: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val savedGitHubRepository =
                    localGitHubRepository.toSavedGitHubRepository(isSaved = false)
                localGitHubDatabaseRepository.removeGitHubRepositoryFromMySavedList(
                    savedGitHubRepository = savedGitHubRepository
                )
                    .flowOn(Dispatchers.IO)
                    .collect { removedResult ->
                        when (removedResult) {
                            is GitHubResponse.Success -> onSuccess()
                            is GitHubResponse.Error -> onError(removedResult.error)
                            is GitHubResponse.Loading -> onLoading()
                        }
                    }
            } catch (ex: Exception) {
                onError(ResponseCode.EXCEPTION)
                logger.error(
                    TAG,
                    ex.message
                        ?: "An error occurred while removing GitHub repository from user's saved list: ${localGitHubRepository.id}",
                    ex
                )
            }
        }
    }
}