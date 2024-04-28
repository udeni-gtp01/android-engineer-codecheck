package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
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
     * Retrieves a selected GitHub repository from the Room database based on its ID.
     *
     * @param selectedGitHubRepositoryId The ID of the repository to be retrieved.
     * @return A `Flow` of `GitHubResponse<LocalGitHubRepository?>` objects. The emitted responses indicate the outcome:
     *   - `Success` containing a `LocalGitHubRepository` object representing the retrieved repository,
     *     or null if no repository with the provided ID is found.
     *   - `Error` containing an error message if any exception occurred during the retrieval process.
     */
    fun getSelectedGitHubRepositoryFromDatabase(selectedGitHubRepositoryId: Long): Flow<GitHubResponse<LocalGitHubRepository?>>
}