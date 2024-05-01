package jp.co.yumemi.android.code_check.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.yumemi.android.code_check.constant.DatabaseConstant

/**
 * Represents a local representation of a GitHub repository stored in the application's Room database.
 * This class models information about a GitHub repository saved by user for later reference, including its core data retrieved from the
 * GitHub API, and additional fields for user interaction tracking.
 *
 *  @param id [PrimaryKey]: Id of the GitHub Repository and the primary key for the Room table.
 *  @param forksCount: The number of forks associated with the GitHub repository (nullable).
 *  @param language: The programming language the GitHub repository is written in (nullable).
 *  @param name: The name of the GitHub repository (nullable).
 *  @param openIssuesCount: The number of open issues currently associated with the GitHub repository (nullable).
 *  @param stargazersCount: The number of users who have starred the GitHub repository (nullable).
 *  @param watchersCount: The number of users watching the GitHub repository (nullable).
 *  @param htmlUrl: The URL to the GitHub repository's HTML page on GitHub (nullable).
 *  @param ownerLogin: The username of the GitHub repository owner (nullable).
 *  @param ownerAvatarUrl: The URL to the owner's avatar image (nullable).
 *  @param isFavorite: An additional field to track whether the user has favorite the GitHub repository). Defaults to `false`.
 *
 *  `@Entity(tableName = ROOM_MY_SAVED_REPO_TABLE_NAME)`: This annotation marks this class as a Room entity,
 *   mapping it to a table named "my_saved_repo_table" within the Room database.
 */
@Entity(tableName = DatabaseConstant.ROOM_MY_SAVED_REPO_TABLE_NAME)
data class SavedGitHubRepository(
    @PrimaryKey val id: Long,
    val forksCount: Long?,
    val language: String?,
    val name: String?,
    val openIssuesCount: Long?,
    val stargazersCount: Long?,
    val watchersCount: Long?,
    val htmlUrl: String?,
    val ownerLogin: String?,
    val ownerAvatarUrl: String?,
    val isFavorite: Boolean = false
)