package jp.co.yumemi.android.code_check.repository

import android.util.Log
import jp.co.yumemi.android.code_check.constant.ResponseCode.EXCEPTION
import jp.co.yumemi.android.code_check.database.GitHubRepositoryDao
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the `LocalGitHubDatabaseRepository` interface. This class interacts with the
 * Room database using the provided `gitHubRepositoryDao` to save and retrieve GitHub repository data.
 */
class LocalGitHubDatabaseRepositoryImpl @Inject constructor(
    private val gitHubRepositoryDao: GitHubRepositoryDao
) : LocalGitHubDatabaseRepository {
    // Logging tag for this class
    private val TAG = this::class.java.simpleName

    /**
     * Suspend function that saves selected Github repository in Room database.
     * This method utilizes Kotlin coroutines and returns a [Flow] of [GitHubResponse] objects.
     *
     * The emitted responses can be of the following types:
     *  * `Success`: Contains a [Boolean] value with the save results.
     *               The response contains `True` if selected Github repository is saved successfully in local database.
     *  * `Error`: Indicates an error occurred during the saving process. The response contains an error message.
     *
     * @param localGitHubRepository The Github repository object to be saved in local database.
     * @return A [Flow] of [GitHubResponse] objects representing the outcome of the save request.
     *
     * @throws Exception Catches unexpected exceptions that might occur during the operation.
     */
    override suspend fun saveSelectedGitHubRepositoryInDatabase(localGitHubRepository: LocalGitHubRepository): Flow<GitHubResponse<Boolean>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                emit(GitHubResponse.Loading) // Indicate loading state
                try {
                    gitHubRepositoryDao.insertGitHubRepository(localGitHubRepository)
                    emit(GitHubResponse.Success(true))
                } catch (ex: Exception) {
                    emit(GitHubResponse.Error(EXCEPTION))
                    val errorMessage =
                        "An unexpected error occurred while saving selected github repository: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                }
            }
        }
    }

    /**
     * Retrieves a selected GitHub repository from the Room database based on its ID.
     *
     * @param selectedGitHubRepositoryId The ID of the repository to be retrieved.
     * @return A `Flow` of `GitHubResponse<LocalGitHubRepository?>` objects. The emitted responses indicate the outcome:
     *   - `Success` containing a `LocalGitHubRepository` object representing the retrieved repository,
     *     or null if no repository with the provided ID is found.
     *   - `Error` containing an error message if any exception occurred during the retrieval process.
     */
    override fun getSelectedGitHubRepositoryFromDatabase(selectedGitHubRepositoryId: Long): Flow<GitHubResponse<LocalGitHubRepository?>> {
        return flow {
            try {
                val response =
                    gitHubRepositoryDao.getGitHubRepositoryById(selectedGitHubRepositoryId)
                emit(GitHubResponse.Success(response))
            } catch (ex: Exception) {
                emit(GitHubResponse.Error(EXCEPTION))
                val errorMessage =
                    "An unexpected error occurred while retrieving selected github repository: ${ex.message}"
                Log.e(TAG, errorMessage, ex)
            }
        }
    }
}