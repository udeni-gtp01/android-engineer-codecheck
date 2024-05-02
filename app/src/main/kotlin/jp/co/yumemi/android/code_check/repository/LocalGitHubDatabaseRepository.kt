package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.model.SavedGitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

/**
 * This interface defines methods for interacting with the local database related to GitHub repositories.
 * It utilizes Kotlin coroutines and Flow for asynchronous data retrieval and emission.
 */
@Singleton
interface LocalGitHubDatabaseRepository {

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
    suspend fun saveSelectedGitHubRepositoryInDatabase(localGitHubRepository: LocalGitHubRepository): Flow<GitHubResponse<Boolean>>

    /**
     * Fetches a `LocalGitHubRepository` object from the database based on primary key [oneId].
     * This `LocalGitHubRepository` is the github repository selected by user to see more info.
     *
     * Since the table contains only one row to store the most recently selected github repository
     * that has fixed value (`1`) as the primary key [oneId], `1` has been passed to retrieve the [LocalGitHubRepository]
     *
     * @return A `Flow` of `GitHubResponse<LocalGitHubRepository?>` objects. The emitted responses indicate the outcome:
     *   - `Success` containing a `LocalGitHubRepository` object representing the retrieved github repository,
     *     or null if no github repository is found.
     *   - `Error` containing an error message if any exception occurred during the retrieval process.
     */
    fun getSelectedGitHubRepositoryFromDatabase(): Flow<GitHubResponse<LocalGitHubRepository?>>

    /**
     * Suspend function that saves selected Github repository in to user's saved list.
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
    suspend fun addGitHubRepositoryToMySavedList(savedGitHubRepository: SavedGitHubRepository): Flow<GitHubResponse<Boolean>>

    /**
     * Suspend function that deleted selected Github repository from user's saved list in database.
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
    suspend fun removeGitHubRepositoryFromMySavedList(savedGitHubRepository: SavedGitHubRepository): Flow<GitHubResponse<Boolean>>

    /**
     * Suspend function that fetches a list `SavedGitHubRepository` objects from the database.
     * This `SavedGitHubRepository` represents a GitHub repository added to user's saved list.
     *
     * @return A `Flow` of `GitHubResponse<List<SavedGitHubRepository>>` object. The emitted responses indicate the outcome:
     *   - `Success` containing a list of `SavedGitHubRepository` objects representing the user's saved list.
     *   - `Error` containing an error message if any exception occurred during the retrieval process.
     */
    suspend fun getMySavedList(): Flow<GitHubResponse<List<SavedGitHubRepository>>>
}