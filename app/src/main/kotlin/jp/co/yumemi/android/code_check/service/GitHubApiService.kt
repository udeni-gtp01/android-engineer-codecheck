package jp.co.yumemi.android.code_check.service

import jp.co.yumemi.android.code_check.constant.Constant
import jp.co.yumemi.android.code_check.model.GitHubRepositoryList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This interface defines methods for interacting with the GitHub API.
 * It provides methods to search for GitHub repositories.
 */
interface GitHubApiService {
    /**
     * Searches for GitHub repositories based on the provided keyword.
     * Utilizes Kotlin coroutines and returns a [Response] object from Retrofit.
     * @param keyWord The keyword to use for searching repositories on GitHub.
     * @return A [Response] object representing the outcome of the search request.
     */
    @GET(Constant.ENDPOINT_GITHUB_REPOSITORIES)
    suspend fun searchRepositories(@Query("q") keyWord: String): Response<GitHubRepositoryList>
}