package jp.co.yumemi.android.code_check.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.co.yumemi.android.code_check.constant.DatabaseConstant.ROOM_GITHUB_REPO_TABLE_NAME
import jp.co.yumemi.android.code_check.constant.DatabaseConstant.ROOM_MY_SAVED_REPO_TABLE_NAME
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.model.SavedGitHubRepository

/**
 * This interface defines the Data Access Object (DAO) for interacting with `LocalGitHubRepository`
 * entity within the application's Room database. It provides methods for inserting and fetching
 * repository data.
 */
@Dao
interface GitHubRepositoryDao {
    /**
     * Inserts a new `LocalGitHubRepository` object into the database to represent the
     * GitHub repository selected by user to see more info from search results list or my saved list.
     * If a record with the same [oneId] (primary key '1') already exists, it is replaced using the provided object.
     *
     * @param localGitHubRepository The `LocalGitHubRepository` object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGitHubRepository(localGitHubRepository: LocalGitHubRepository)

    /**
     * Fetches a `LocalGitHubRepository` object from the database based on primary key [oneId].
     * This `LocalGitHubRepository` is the GitHub repository selected by user to see more info.
     *
     * Since the table contains only one row to store the most recently selected GitHub repository
     * and that has fixed value (`1`) as the primary key [oneId], `1` has been passed to retrieve
     * the [LocalGitHubRepository].
     *
     * @return A [LocalGitHubRepository] object representing the selected GitHub repository by user
     * to see more info, or null if no repository is found as selected GitHub repository.
     */
    @Query("SELECT * FROM $ROOM_GITHUB_REPO_TABLE_NAME WHERE oneId = 1")
    suspend fun getSelectedGitHubRepository(): LocalGitHubRepository?

    /**
     * Inserts a new `SavedGitHubRepository` object into the database to represent the
     * GitHub repository added to My saved list in the database.
     *
     * @param savedGitHubRepository The `SavedGitHubRepository` object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedGitHubRepository(savedGitHubRepository: SavedGitHubRepository)

    /**
     * Delete inserted `SavedGitHubRepository` object from the database. Then the deleted
     * GitHub repository will not be shown in My saved list.
     *
     * @param savedGitHubRepository The `SavedGitHubRepository` object to be deleted.
     */
    @Delete
    suspend fun deleteSavedGitHubRepository(savedGitHubRepository: SavedGitHubRepository)

    /**
     * Fetches a list of `SavedGitHubRepository` objects from the database
     * representing the My saved list of GitHub repositories.
     * This `SavedGitHubRepository` represents a GitHub repository added to My saved list.
     *
     * @return A a list of [SavedGitHubRepository] objects representing My saved list.
     */
    @Query("SELECT * FROM $ROOM_MY_SAVED_REPO_TABLE_NAME")
    fun getSavedGitHubRepositories(): List<SavedGitHubRepository>
}