package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.service.GithubApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubRepository @Inject constructor(private val githubApiService: GithubApiService) {

    suspend fun searchRepositoryList(inputText: String): List<RepositoryItem>? {
        return withContext(Dispatchers.IO) {
            return@withContext getGithubApiResponse(inputText)?.items
        }
    }

    private suspend fun getGithubApiResponse(inputText: String): GitHubResponse? {
        var gitHubResponse: GitHubResponse? = null
        val response = githubApiService.searchRepositories(inputText)
        if (response.isSuccessful) {
            gitHubResponse = response.body()
        }
        return gitHubResponse
    }
}
