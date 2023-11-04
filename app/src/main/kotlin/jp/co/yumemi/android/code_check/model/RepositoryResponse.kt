package jp.co.yumemi.android.code_check.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents the data model for GitHub API response.
 * This class captures the relevant information and properties returned by the GitHub API.
 */
@Parcelize
data class GitHubResponse(

    val items: List<RepositoryItem>

) : Parcelable

/**
 * Represents a repository in an array of items in the GitHub API response.
 * It contains properties related to a repository, such as forks count, language, name, etc.
 */
@Parcelize
data class RepositoryItem(

    val id: String,

    @SerializedName("forks_count") val forksCount: Long,

    val language: String?,

    @SerializedName("name") val name: String,

    @SerializedName("open_issues_count") val openIssuesCount: Long,

    val owner: Owner,

    @SerializedName("stargazers_count") val stargazersCount: Long,

    @SerializedName("watchers_count") val watchersCount: Long

) : Parcelable

/**
 * Represents the owner of a repository.
 * It contains properties related to the owner, such as avatar URL.
 */
@Parcelize
data class Owner(

    @SerializedName("login") val login: String,

    @SerializedName("avatar_url") val avatarUrl: String

) : Parcelable