package jp.co.yumemi.android.code_check.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.constants.Constant
import jp.co.yumemi.android.code_check.repository.GithubRepository
import jp.co.yumemi.android.code_check.service.GithubApiService
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        return okHttpClient.build()
    }

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

    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return Constant.BASE_URL
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create();
    }

    @Singleton
    @Provides
    fun provideGithubApiService(retrofit: Retrofit): GithubApiService {
        return retrofit.create(GithubApiService::class.java)
    }
    @Singleton
    @Provides
    fun provideGithubRepository(githubApiService: GithubApiService): GithubRepository {
        return GithubRepository(githubApiService)
    }
}