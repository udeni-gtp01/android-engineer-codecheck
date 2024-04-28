package jp.co.yumemi.android.code_check.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.co.yumemi.android.code_check.constant.DatabaseConstant.ROOM_GITHUB_REPO_TABLE_NAME
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository

/**
 * This interface defines the Data Access Object (DAO) for interacting with `LocalGitHubRepository`
 * entities within the application's Room database. It provides methods for inserting and fetching
 * repository data.
 */
@Dao
interface GitHubRepositoryDao {
    /**
     * Inserts a new `LocalGitHubRepository` object into the database.
     * If a record with the same ID already exists, it is replaced using the provided object.
     *
     * @param localGitHubRepository The `LocalGitHubRepository` object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGitHubRepository(localGitHubRepository: LocalGitHubRepository)

    /**
     * Fetches a `LocalGitHubRepository` object from the database based on its ID.
     * This `LocalGitHubRepository` is the github repository selected by user to see more info.
     *
     * @param id The ID of the repository to be retrieved.
     * @return A `LocalGitHubRepository` object representing the retrieved repository, or null
     * if no repository with the provided ID is found.
     */
    @Query("SELECT * FROM $ROOM_GITHUB_REPO_TABLE_NAME WHERE id = :id")
    suspend fun getGitHubRepositoryById(id: Long): LocalGitHubRepository?
}