package jp.co.yumemi.android.code_check.service

import dagger.Provides
import jp.co.yumemi.android.code_check.constants.Constant
import jp.co.yumemi.android.code_check.model.ServerResponse
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton

interface GithubApiService {
    @GET(Constant.ENDPOINT_REPOSITORY)
    suspend fun getRepositories():Response<ServerResponse>
}
