package jp.co.yumemi.android.code_check.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.constant.Constant.BASE_URL
import jp.co.yumemi.android.code_check.repository.GitHubApiRepository
import jp.co.yumemi.android.code_check.repository.GitHubApiRepositoryImpl
import jp.co.yumemi.android.code_check.service.GitHubApiService
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * This Dagger module provides all network-related dependencies used throughout the application.
 * It configures and creates singletons for OkHttpClient, Retrofit, and the GitHubApiService interface.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * Provides a singleton instance of OkHttpClient configured for use with the API.
     *
     * This currently uses the default OkHttpClient configuration and adds a [NetworkInterceptor]
     * which sets a custom "Accept" header in all outgoing requests.
     *
     * @return A singleton OkHttpClient instance.
     */
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val okHttpClient =
            OkHttpClient.Builder().addInterceptor(NetworkInterceptor())
        return okHttpClient.build()
    }

    /**
     * Provides an instance of Retrofit configured for interacting with the GitHub API.
     *
     * @param baseUrl The base URL for the GitHub API.
     * @param okHttpClient The OkHttpClient instance used for network requests (currently configured with interceptor).
     * @param converterFactory The Converter.Factory used for converting between JSON and POJOs (Gson by default).
     * @return A singleton Retrofit instance configured for interacting with the GitHub API.
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
        return BASE_URL
    }

    /**
     * Provides a singleton instance of GsonConverterFactory used for converting between JSON and POJOs by default.
     *
     * GsonConverterFactory is a popular choice for serializing and deserializing data between application objects (POJOs)
     * and JSON format used in network communication.
     *
     * @return A singleton GsonConverterFactory instance.
     */
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    /**
     * Provides a singleton instance of the GitHubApiService interface.
     * which defines methods for interacting with the GitHub API endpoints. Retrofit uses reflection to create an implementation
     * of this interface based on the provided Retrofit instance and the annotations on the interface methods.
     *
     * @param retrofit The Retrofit instance configured for interacting with the GitHub API.
     * @return A singleton instance of the GitHubApiService interface.
     */
    @Singleton
    @Provides
    fun provideGithubApiService(retrofit: Retrofit): GitHubApiService {
        return retrofit.create(GitHubApiService::class.java)
    }

    /**
     * Provides a singleton instance of the GitHubApiRepository interface
     * which defines methods for interacting with the GitHub API related to repositories.
     *
     * This method injects the required `GitHubApiService` dependency and returns an instance of the
     * `GitHubApiRepositoryImpl` class which implements the `GitHubApiRepository` interface.
     *
     * @param gitHubApiService The injected instance of the GitHubApiService interface.
     * @return A singleton instance of the GitHubApiRepository interface.
     */
    @Singleton
    @Provides
    fun provideGitHubApiRepository(gitHubApiService: GitHubApiService): GitHubApiRepository {
        return GitHubApiRepositoryImpl(gitHubApiService)
    }
}