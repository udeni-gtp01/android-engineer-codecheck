package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.ServerResult
import jp.co.yumemi.android.code_check.service.GithubApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/**
 * Repository class responsible for interacting with the GitHub API and fetching repository data.
 * It provides a method to search for repository items based on the provided input text.
 *
 * @param githubApiService The service responsible for making API requests to the GitHub API.
 */
class GithubRepository @Inject constructor(private val githubApiService: GithubApiService) {

    /**
     * Searches for repository items based on the provided input text.
     *
     * @param inputText The text used for searching repositories.
     * @return A list of repository items matching the search query, or null if the search failed.
     * FIXME: When search requests are being made too frequently the return response is empty.
     */
    suspend fun searchRepositoryList(inputText: String): ServerResult<GitHubResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext getGithubApiResponse(inputText)
        }
    }

    /**
     * Fetches the GitHub API response for the provided input text.
     *
     * @param inputText The text used for searching repositories.
     * @return The GitHub API response, or null if the request failed.
     */
    private suspend fun getGithubApiResponse(inputText: String): ServerResult<GitHubResponse> {
        return try {
            val response = githubApiService.searchRepositories(inputText)
            if (response.isSuccessful) {
                ServerResult.Success(response.body() ?: GitHubResponse(emptyList()))
            } else {
                ServerResult.ProcessingError(response.errorBody().toString())
            }
        } catch (error: IOException) {
            // Handle network connection issues, e.g., no internet connection
            ServerResult.NetworkError(error.localizedMessage)

        } catch (error: Exception) {
            ServerResult.ProcessingError(error.localizedMessage)
        }
    }
}