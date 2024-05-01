package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.constant.ResponseCode.EXCEPTION
import jp.co.yumemi.android.code_check.database.GitHubRepositoryDao
import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.model.SavedGitHubRepository
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
    private val gitHubRepositoryDao: GitHubRepositoryDao,
    private val logger: Logger
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
                    logger.error(TAG, errorMessage, ex)
                }
            }
        }
    }

    /**
     * Retrieves a selected GitHub repository from the [LocalGitHubRepository] table in Room database.
     * The [LocalGitHubRepository] table contains only one row to store the most recently selected
     * github repository and that has fixed value (`1`) as the primary key.
     *
     * @return A `Flow` of `GitHubResponse<LocalGitHubRepository?>` object.
     * The emitted responses indicate the outcome:
     *   - `Success` containing a `LocalGitHubRepository` object representing the retrieved github repository,
     *     or null if no github repository is found.
     *   - `Error` containing an error message if any exception occurred during the retrieval process.
     */
    override fun getSelectedGitHubRepositoryFromDatabase(): Flow<GitHubResponse<LocalGitHubRepository?>> {
        return flow {
            try {
                val response =
                    gitHubRepositoryDao.getSelectedGitHubRepository()
                emit(GitHubResponse.Success(response))
            } catch (ex: Exception) {
                emit(GitHubResponse.Error(EXCEPTION))
                val errorMessage =
                    "An unexpected error occurred while retrieving selected github repository: ${ex.message}"
                logger.error(TAG, errorMessage, ex)
            }
        }
    }

    /**
     * Suspend function that saves selected Github repository in to My saved list.
     * This method utilizes Kotlin coroutines and returns a [Flow] of [GitHubResponse] objects.
     *
     * The emitted responses can be of the following types:
     *  * `Success`: Contains a [Boolean] value with the save results.
     *               The response contains `True` if selected Github repository is saved successfully in local database.
     *  * `Error`: Indicates an error occurred during the saving process. The response contains an error message.
     *
     * @param savedGitHubRepository The Github repository object to be saved in local database.
     * @return A [Flow] of [GitHubResponse] objects representing the outcome of the save request.
     *
     * @throws Exception Catches unexpected exceptions that might occur during the operation.
     */
    override suspend fun addGitHubRepositoryToMySavedList(savedGitHubRepository: SavedGitHubRepository): Flow<GitHubResponse<Boolean>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                emit(GitHubResponse.Loading) // Indicate loading state
                try {
                    gitHubRepositoryDao.insertSavedGitHubRepository(savedGitHubRepository)
                    emit(GitHubResponse.Success(true))
                } catch (ex: Exception) {
                    emit(GitHubResponse.Error(EXCEPTION))
                    val errorMessage =
                        "An unexpected error occurred while saving selected github repository: ${ex.message}"
                    logger.error(TAG, errorMessage, ex)
                }
            }
        }
    }

    /**
     * Suspend function that deleted selected Github repository from My saved list in database.
     * This method utilizes Kotlin coroutines and returns a [Flow] of [GitHubResponse] objects.
     *
     * The emitted responses can be of the following types:
     *  * `Success`: Contains a [Boolean] value with the delete results.
     *               The response contains `True` if selected Github repository is deleted successfully from local database.
     *  * `Error`: Indicates an error occurred during the deleting process. The response contains an error message.
     *
     * @param savedGitHubRepository The Github repository object to be deleted from local database.
     * @return A [Flow] of [GitHubResponse] objects representing the outcome of the delete request.
     *
     * @throws Exception Catches unexpected exceptions that might occur during the operation.
     */
    override suspend fun removeGitHubRepositoryFromMySavedList(savedGitHubRepository: SavedGitHubRepository): Flow<GitHubResponse<Boolean>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                emit(GitHubResponse.Loading) // Indicate loading state
                try {
                    gitHubRepositoryDao.deleteSavedGitHubRepository(savedGitHubRepository)
                    emit(GitHubResponse.Success(true))
                } catch (ex: Exception) {
                    emit(GitHubResponse.Error(EXCEPTION))
                    val errorMessage =
                        "An unexpected error occurred while deleting the saved github repository: ${ex.message}"
                    logger.error(TAG, errorMessage, ex)
                }
            }
        }
    }

    /**
     * Suspend function that fetches a list `SavedGitHubRepository` objects from the database.
     * This `SavedGitHubRepository` represents a GitHub repository added to My saved list.
     *
     * @return A `Flow` of `GitHubResponse<List<SavedGitHubRepository>>` object. The emitted responses indicate the outcome:
     *   - `Success` containing a list of `SavedGitHubRepository` objects representing the My saved list.
     *   - `Error` containing an error message if any exception occurred during the retrieval process.
     */
    override suspend fun getMySavedList(): Flow<GitHubResponse<List<SavedGitHubRepository>>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                emit(GitHubResponse.Loading) // Indicate loading state
                try {
                    val response = gitHubRepositoryDao.getSavedGitHubRepositories()
                    emit(GitHubResponse.Success(response))
                } catch (ex: Exception) {
                    emit(GitHubResponse.Error(EXCEPTION))
                    val errorMessage =
                        "An unexpected error occurred while retrieving My saved list: ${ex.message}"
                    logger.error(TAG, errorMessage, ex)
                }
            }
        }
    }
}