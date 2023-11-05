package jp.co.yumemi.android.code_check.model

sealed class ServerResult<out T> {
    data class Success<out T>(val data: T) : ServerResult<T>()
    data class ProcessingError(val message: String?) : ServerResult<Nothing>()
    data class NetworkError(val message: String?) : ServerResult<Nothing>()
    data object Loading : ServerResult<Nothing>()
}