package jp.co.yumemi.android.code_check.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents the data model for GitHub API response.
 * This class captures the relevant information and properties returned by the server in response to HTTP request.
 */
@Parcelize
data class ServerResponse(
    val items: List<RepositoryItem>
) : Parcelable
@Parcelize
data class RepositoryItem(
    @SerializedName("full_name")
    val name: String,
    val owner: Owner,
    //val ownerIconUrl: String,
    val language: String,
    @SerializedName("stargazers_count")
    val stargazersCount: Long,
    @SerializedName("watchers_count")
    val watchersCount: Long,
    @SerializedName("forks_count")
    val forksCount: Long,
    @SerializedName("open_issues_count")
    val openIssuesCount: Long,
) : Parcelable
@Parcelize
data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl: String
) : Parcelable