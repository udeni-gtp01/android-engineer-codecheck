package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.model.ServerResponse
import jp.co.yumemi.android.code_check.service.GithubApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubRepository @Inject constructor(private val githubApiService: GithubApiService) {

    suspend fun getRepositoryList(inputText: String):List<RepositoryItem>?{
        return withContext(Dispatchers.IO){
            return@withContext getGithubApiResponse(inputText)?.items
        }
    }
    private suspend fun getGithubApiResponse(inputText: String):ServerResponse?{
        var serverResponse:ServerResponse?=null
        val response=githubApiService.getRepositories(inputText)
        if (response.isSuccessful){
            serverResponse=response.body()
        }
        return serverResponse
    }
}
