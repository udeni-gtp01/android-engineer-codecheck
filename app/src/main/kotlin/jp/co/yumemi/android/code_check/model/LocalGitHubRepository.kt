package jp.co.yumemi.android.code_check.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.yumemi.android.code_check.constant.DatabaseConstant.ROOM_GITHUB_REPO_TABLE_NAME

/**
 * Represents a local representation of a GitHub repository stored in the application's Room database.
 * This class models information about a specific repository, including its core data retrieved from the
 * GitHub API, and additional fields for user interaction tracking.
 *
 *  @param oneId [PrimaryKey]: This field serves as the primary key for the Room table.
 *      - Setting a fixed value (`1`) ensures a single row exists in the table. This approach is
 *        used to store GitHub repository selected by user to see more info from search results list.
 *  @param id: Id of the GitHub Repository.
 *  @param forksCount: The number of forks associated with the GitHub repository (nullable).
 *  @param language: The programming language the GitHub repository is written in (nullable).
 *  @param name: The name of the GitHub repository (nullable).
 *  @param openIssuesCount: The number of open issues currently associated with the GitHub repository (nullable).
 *  @param stargazersCount: The number of users who have starred the GitHub repository (nullable).
 *  @param watchersCount: The number of users watching the GitHub repository (nullable).
 *  @param htmlUrl: The URL to the GitHub repository's HTML page on GitHub (nullable).
 *  @param ownerLogin: The username of the GitHub repository owner (nullable).
 *  @param ownerAvatarUrl: The URL to the owner's avatar image (nullable).
 *  @param isSaved: To track whether the user has added the GitHub repository to user's saved list.
 *
 *  `@Entity(tableName = ROOM_GITHUB_REPO_TABLE_NAME)`: This annotation marks this class as a Room entity,
 *   mapping it to a table named "github_repo_table" within the Room database.
 */
@Entity(tableName = ROOM_GITHUB_REPO_TABLE_NAME)
data class LocalGitHubRepository(
    @PrimaryKey val oneId: Byte = 1,
    val id: Long,
    val forksCount: Long?,
    val language: String?,
    val name: String?,
    val openIssuesCount: Long?,
    val stargazersCount: Long?,
    val watchersCount: Long?,
    val htmlUrl: String?,
    val ownerLogin: String?,
    val ownerAvatarUrl: String?,
    val isSaved: Boolean
)

/**
 * Converts a [GitHubRepository] object to a corresponding [SavedGitHubRepository] object.
 *
 * @param isSaved Boolean indicating whether the repository is saved in the user's saved list in local database.
 * @return A [SavedGitHubRepository] object with properties copied from the [LocalGitHubRepository] object.
 */
fun LocalGitHubRepository.toSavedGitHubRepository(isSaved: Boolean): SavedGitHubRepository {
    return SavedGitHubRepository(
        id = this.id,
        forksCount = this.forksCount,
        language = this.language,
        name = this.name,
        openIssuesCount = this.openIssuesCount,
        stargazersCount = this.stargazersCount,
        watchersCount = this.watchersCount,
        htmlUrl = this.htmlUrl,
        ownerLogin = this.ownerLogin,
        ownerAvatarUrl = this.ownerAvatarUrl,
        isSaved = isSaved
    )
}