package jp.co.yumemi.android.code_check.model

/**
 * This sealed class represents the possible responses from a GitHub API call.
 * It can be one of the following types:
 *  * `Success`: Represents a successful API call with the requested data in the `data` property.
 *  * `Error`: Represents an error that occurred during the API call. It contains an optional `message` explaining the error.
 *  * `Loading`: Indicates that the API call is still in progress and data is not yet available.
 *
 * @param T The type of data expected in the successful response (`Success`).
 */
sealed class GitHubResponse<out T> {

    /**
     * Represents a successful response from a GitHub API call.
     *
     * @param T The type of data contained in the response.
     * @property data The requested data retrieved from the API call.
     */
    data class Success<out T>(val data: T) : GitHubResponse<T>()

    /**
     * Represents an error that occurred during a GitHub API call.
     *
     * @property message An optional message explaining the error (can be null).
     */
    data class Error(val message: String?) : GitHubResponse<Nothing>()

    /**
     * Represents the state when a GitHub API call is still in progress and data is not yet available.
     */
    data object Loading : GitHubResponse<Nothing>()
}