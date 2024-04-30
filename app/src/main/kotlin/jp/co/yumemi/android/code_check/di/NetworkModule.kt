package jp.co.yumemi.android.code_check.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.constant.ApiEndpoint
import jp.co.yumemi.android.code_check.constant.ApiEndpoint.BASE_URL
import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.logger.LoggerImpl
import jp.co.yumemi.android.code_check.repository.GitHubApiRepository
import jp.co.yumemi.android.code_check.repository.GitHubApiRepositoryImpl
import jp.co.yumemi.android.code_check.service.GitHubApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
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
    fun provideGitHubApiRepository(
        gitHubApiService: GitHubApiService,
        logger: Logger
    ): GitHubApiRepository {
        return GitHubApiRepositoryImpl(gitHubApiService = gitHubApiService, logger = logger)
    }

    /**
     * Provides a singleton instance of the `Logger` interface. This function uses Dagger's
     *
     * The current implementation returns an instance of `LoggerImpl`.
     *
     * @return A singleton instance of the `Logger` interface.
     */
    @Singleton
    @Provides
    fun provideLogger(): Logger {
        return LoggerImpl()
    }
}

class NetworkInterceptor : Interceptor {
    /**
     * Intercepts the network request, modifies headers, and proceeds with the request.
     *
     * @param chain The OkHttp Interceptor Chain for handling the request.
     * @return The response received after processing the modified request.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val modifiedRequest = originalRequest.newBuilder()
            .header("Accept", ApiEndpoint.HEADER_TYPE)
            .build()
        return chain.proceed(modifiedRequest)
    }
}