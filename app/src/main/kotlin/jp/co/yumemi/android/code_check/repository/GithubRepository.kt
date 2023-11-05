package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.ServerResult

/**
 * Interface for searching GitHub repositories.
 */
interface GithubRepository {
    /**
     * Searches for repository items based on the provided input text.
     *
     * @param inputText The text used for searching repositories.
     * @return A [ServerResult] containing the result of the search operation. If successful,
     * it returns a list of [GitHubResponse] items matching the search query. If the search fails,
     * it returns an appropriate error or network-related result.
     */
    suspend fun searchRepositoryList(inputText: String): ServerResult<GitHubResponse>
}