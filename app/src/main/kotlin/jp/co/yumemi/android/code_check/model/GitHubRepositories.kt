package jp.co.yumemi.android.code_check.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents a list of GitHub repositories received from the GitHub API response.
 * Implements the Parcelable interface for easy serialization/deserialization.
 * @property items A list of repository items retrieved from the GitHub API response.
 */
@Parcelize
data class GitHubRepositoryList(
    val items: List<GitHubRepository>
) : Parcelable

/**
 * Represents a single repository within the GitHub API response.
 * Contains properties related to a repository, such as its forks count, programming language,
 * name, open issues count, owner details, star count, watcher count, and HTML URL.
 * Implements the Parcelable interface for easy serialization/deserialization.
 *
 * @property forksCount The number of forks the repository has.
 * @property language The programming language the repository is written in.
 * @property name The name of the repository.
 * @property openIssuesCount The number of open issues the repository has.
 * @property owner Details of the repository owner.
 * @property stargazersCount The number of users who have starred the repository.
 * @property watchersCount The number of users watching the repository.
 * @property htmlUrl The URL to the repository on GitHub.
 *
 * Properties are annotated with `@SerializedName` to specify the corresponding JSON field names from the API response.
 */
@Parcelize
data class GitHubRepository(
    val id: Long,
    @SerializedName("forks_count") val forksCount: Long?,
    val language: String?,
    val name: String?,
    @SerializedName("open_issues_count") val openIssuesCount: Long?,
    val owner: GitHubRepositoryOwner?,
    @SerializedName("stargazers_count") val stargazersCount: Long?,
    @SerializedName("watchers_count") val watchersCount: Long?,
    @SerializedName("html_url") val htmlUrl: String?
) : Parcelable

/**
 * Represents the owner of a GitHub repository within the API response.
 * Contains properties related to the repository owner, such as their username and avatar URL.
 * Implements the Parcelable interface for easy serialization/deserialization.
 *
 * @property login The username of the repository owner.
 * @property avatarUrl The URL to the owner's avatar image.
 *
 * Properties are annotated with `@SerializedName` to specify the corresponding JSON field names from the API response.
 */
@Parcelize
data class GitHubRepositoryOwner(
    @SerializedName("login") val login: String?,
    @SerializedName("avatar_url") val avatarUrl: String?
) : Parcelable

/**
 * Converts a [GitHubRepository] object to a corresponding [LocalGitHubRepository] object.
 *
 * @param isSaved Boolean indicating whether the repository is saved in the user's saved list in local database.
 * @return A [LocalGitHubRepository] object with properties copied from the [GitHubRepository] object.
 */
fun GitHubRepository.toLocalGitHubRepository(isSaved: Boolean): LocalGitHubRepository {
    return LocalGitHubRepository(
        id = this.id,
        forksCount = this.forksCount,
        language = this.language,
        name = this.name,
        openIssuesCount = this.openIssuesCount,
        stargazersCount = this.stargazersCount,
        watchersCount = this.watchersCount,
        htmlUrl = this.htmlUrl,
        ownerLogin = this.owner?.login,
        ownerAvatarUrl = this.owner?.avatarUrl,
        isSaved = isSaved
    )
}