package jp.co.yumemi.android.code_check.model

sealed class ServerResult<out T> {
    data class Success<out T>(val data: T) : ServerResult<T>()
    data class Error(val message: String) : ServerResult<Nothing>()
    data object Loading : ServerResult<Nothing>()
}