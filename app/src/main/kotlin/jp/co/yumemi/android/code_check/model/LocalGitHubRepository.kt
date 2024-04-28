package jp.co.yumemi.android.code_check.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.yumemi.android.code_check.constant.DatabaseConstant.ROOM_GITHUB_REPO_TABLE_NAME

/**
 * Represents a local representation of a GitHub repository stored in the application's Room database.
 * This data class holds information about a specific repository, including its ID, various counts
 * (forks, open issues, stars, watchers), language, name, and URLs for the HTML page and owner avatar.
 *
 * This class is annotated with `@Entity(tableName = ROOM_GITHUB_REPO_TABLE_NAME)` to indicate
 * that it maps to a table named "github_repo_table" within the Room database.
 */
@Entity(tableName = ROOM_GITHUB_REPO_TABLE_NAME)
data class LocalGitHubRepository(
    @PrimaryKey val id: Long,
    val forksCount: Long?,
    val language: String?,
    val name: String?,
    val openIssuesCount: Long?,
    val stargazersCount: Long?,
    val watchersCount: Long?,
    val htmlUrl: String?,
    val ownerLogin: String?,
    val ownerAvatarUrl: String?
)