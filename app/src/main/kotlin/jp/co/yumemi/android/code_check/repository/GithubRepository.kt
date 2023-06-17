package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.ServerResponse
import jp.co.yumemi.android.code_check.service.GithubApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubRepository @Inject constructor(private val githubApiService: GithubApiService) {

    suspend fun getRepositoriesFromDataSource():ServerResponse?{
        return withContext(Dispatchers.IO){
            return@withContext getRepositoriesFromRemoteService()
        }
    }
    private suspend fun getRepositoriesFromRemoteService():ServerResponse?{
        var repositories:ServerResponse?=null
        val response=githubApiService.getRepositories()
        if (response.isSuccessful){
            repositories=response.body()
        }
        return repositories
    }
}
