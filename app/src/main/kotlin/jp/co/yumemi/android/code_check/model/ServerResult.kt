package jp.co.yumemi.android.code_check.model

/**
 * A sealed class representing the result of a server operation, which can be one of the following:
 *
 * - [Success] with associated data of type [T]: Represents a successful result with the retrieved data.
 * - [ProcessingError] with an optional error [message]: Indicates an error during processing on the server.
 * - [NetworkError] with an optional error [message]: Indicates a network-related error.
 * - [Loading]: Represents that the operation is in progress, typically used for loading indicators.
 *
 * @param T The type of data associated with a [Success] result.
 */
sealed class ServerResult<out T> {
    data class Success<out T>(val data: T) : ServerResult<T>()
    data class ProcessingError(val message: String?) : ServerResult<Nothing>()
    data class NetworkError(val message: String?) : ServerResult<Nothing>()
    data object Loading : ServerResult<Nothing>()
}