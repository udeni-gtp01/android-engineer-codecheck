package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.GitHubRepositoryList
import jp.co.yumemi.android.code_check.model.GitHubResponse
import kotlinx.coroutines.flow.Flow

/**
 * This interface defines methods for interacting with the GitHub API related to repositories.
 */
interface GitHubApiRepository {
    /**
     * This suspend function searches for GitHub repositories based on the provided input text.
     * It utilizes Kotlin coroutines and returns a [Flow] of [GitHubResponse] objects.
     *
     * The `Flow` returned by this function can emit different types of responses:
     *  * `Success`: Contains a [GitHubRepositoryList] object with the search results if successful.
     *  * `Error`: Indicates an error occurred during the API call. The response contains an optional error message.
     *
     * @param inputText The search query string to be used for finding repositories on GitHub.
     * @return A [Flow] of [GitHubResponse] objects representing the outcome of the search request.
     */
    suspend fun searchGitHubRepositories(inputText: String): Flow<GitHubResponse<GitHubRepositoryList>>
}