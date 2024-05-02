package jp.co.yumemi.android.code_check.logger

import android.util.Log

/**
 * An interface for logging errors in the application.
 * Implementations of this interface define how error messages are logged.
 */
interface Logger {
    /**
     * Logs an error message with the specified tag and optional throwable.
     *
     * @param tag A string tag to identify the source of the log message.
     * @param message The error message to be logged.
     * @param throwable An optional [Throwable] object representing the error cause or null if none.
     */
    fun error(tag: String, message: String, throwable: Throwable?)
}

/**
 * Implementation of the [Logger] interface using Android's built-in logging mechanism.
 * Logs error messages using Android's Log.e method.
 */
class LoggerImpl : Logger {
    /**
     * Logs an error message using Android's built-in logging mechanism.
     * The message is logged with an error level.
     *
     * @param tag A string tag to identify the source of the log message.
     * @param message The error message to be logged.
     * @param throwable An optional [Throwable] object representing the error cause or null if none.
     */
    override fun error(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}