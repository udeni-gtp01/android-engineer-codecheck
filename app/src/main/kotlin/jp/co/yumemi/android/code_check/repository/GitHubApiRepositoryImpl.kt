package jp.co.yumemi.android.code_check.repository

import android.util.Log
import jp.co.yumemi.android.code_check.constant.ResponseCode.EXCEPTION
import jp.co.yumemi.android.code_check.constant.ResponseCode.IOEXCEPTION
import jp.co.yumemi.android.code_check.constant.ResponseCode.TIMEOUT_EXCEPTION
import jp.co.yumemi.android.code_check.model.GitHubRepositoryList
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.service.GitHubApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Implementation of the [GitHubApiRepository] interface that interacts with the GitHub API using a provided
 * [GitHubApiService] instance (injected through Dagger).
 */
class GitHubApiRepositoryImpl @Inject constructor(private val gitHubApiService: GitHubApiService) :
    GitHubApiRepository {
    // Logging tag for this class
    private val TAG = this::class.java.simpleName

    /**
     * Suspend function that searches for GitHub repositories based on the provided input text.
     * This method utilizes Kotlin coroutines and returns a [Flow] of [GitHubResponse] objects.
     *
     * The emitted responses can be of the following types:
     *  * `Success`: Contains a [GitHubRepositoryList] object with the search results if successful.
     *  * `Error`: Indicates an error occurred during the API call. The response contains an error message.
     *
     * @param inputText The search query string to be used for finding repositories on GitHub.
     * @return A [Flow] of [GitHubResponse] objects representing the outcome of the search request.
     *
     * @throws IOException Signals network-related errors during the API call.
     * @throws SocketTimeoutException Signals a timeout exception if the connection to the GitHub API takes too long.
     * @throws Exception Catches any other unexpected exceptions that might occur during the operation.
     */
    override suspend fun searchGitHubRepositories(inputText: String): Flow<GitHubResponse<GitHubRepositoryList>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                emit(GitHubResponse.Loading) // Indicate loading state
                try {
                    val response = gitHubApiService.searchRepositories(inputText)
                    if (response.isSuccessful) {
                        emit(
                            GitHubResponse.Success(
                                response.body() ?: GitHubRepositoryList(
                                    emptyList()
                                )
                            )
                        )
                    } else {
                        emit(GitHubResponse.Error(response.code().toString()))
                        val errorMessage =
                            "Failed to search GitHub repositories for keyword $inputText: ${
                                response.errorBody()?.string()
                            }"
                        Log.e(TAG, errorMessage)
                    }
                } catch (ex: IOException) {
                    // Emit a failure result for network errors
                    emit(GitHubResponse.Error(IOEXCEPTION))
                    val errorMessage = "Network error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: SocketTimeoutException) {
                    // Emit a failure result for connection timeout errors
                    emit(GitHubResponse.Error(TIMEOUT_EXCEPTION))
                    val errorMessage = "Connection timed out: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: Exception) {
                    // Emit a failure result for unexpected errors
                    emit(GitHubResponse.Error(EXCEPTION))
                    val errorMessage = "An unexpected error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                }
            }
        }
    }
}