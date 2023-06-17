package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.ServerResponse
import jp.co.yumemi.android.code_check.service.GithubApiService
import javax.inject.Inject

class GithubRepository @Inject constructor(private val githubApiService: GithubApiService) {
    private suspend fun getRepositoriesFromRemoteService():ServerResponse?{
        var repositories:ServerResponse?=null
        val response=githubApiService.getRepositories()
        if (response.isSuccessful){
            repositories=response.body()
        }
        return repositories
    }
}
