package jp.co.yumemi.android.code_check.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents the data model for GitHub API response.
 * This class captures the relevant information and properties returned by the server in response to HTTP request.
 */
@Parcelize
data class RepositoryResponse(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable
