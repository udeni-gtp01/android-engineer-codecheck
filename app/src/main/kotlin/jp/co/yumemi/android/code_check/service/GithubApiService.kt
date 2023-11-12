package jp.co.yumemi.android.code_check.service

import jp.co.yumemi.android.code_check.constant.Constant
import jp.co.yumemi.android.code_check.model.GitHubResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Service interface for communicating with the GitHub API.
 */
interface GithubApiService {

    /**
     * Searches repositories on GitHub based on the specified query.
     *
     * @param keyWord The search query.
     * @return A [Response] containing the search results as [GitHubResponse].
     */
    @Headers(Constant.HEADER_1)
    @GET(Constant.ENDPOINT_REPOSITORY)
    suspend fun searchRepositories(@Query("q") keyWord: String): Response<GitHubResponse>
}
