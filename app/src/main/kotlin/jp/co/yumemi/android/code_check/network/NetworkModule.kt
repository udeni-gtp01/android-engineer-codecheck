package jp.co.yumemi.android.code_check.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.constant.Constant
import jp.co.yumemi.android.code_check.repository.GithubRepository
import jp.co.yumemi.android.code_check.repository.GithubRepositoryImpl
import jp.co.yumemi.android.code_check.service.GithubApiService
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dependency injection using Hilt module that provides network-related functions.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides an instance of OkHttpClient.
     */
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        return okHttpClient.build()
    }

    /**
     * Provides an instance of Retrofit.
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String, okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
        return retrofit.build()
    }

    /**
     * Provides the base URL for the API.
     */
    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return Constant.BASE_URL
    }

    /**
     * Provides an instance of GsonConverterFactory.
     */
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    /**
     * Provides an instance of GithubApiService.
     */
    @Singleton
    @Provides
    fun provideGithubApiService(retrofit: Retrofit): GithubApiService {
        return retrofit.create(GithubApiService::class.java)
    }

    /**
     * Provides an instance of GithubRepository.
     */
    @Singleton
    @Provides
    fun provideGithubRepository(githubApiService: GithubApiService): GithubRepository {
        return GithubRepositoryImpl(githubApiService)
    }
}