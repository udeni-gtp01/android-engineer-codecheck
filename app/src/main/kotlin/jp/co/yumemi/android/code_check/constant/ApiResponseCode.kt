package jp.co.yumemi.android.code_check.constant

/**
 * Singleton object containing constant response codes commonly used in HTTP requests.
 * These codes represent different types of responses as defined by the HTTP specification:
 *
 *  * Informational (100-199): Used to convey informational messages or progress without requiring any action from the client.
 *  * Successful (200-299): Indicate that the request was successfully processed. The specific code provides more details about the result.
 *  * Redirection (300-399): Indicate that further action needs to be taken by the client to complete the request, typically involving redirection to a different URL.
 *  * Client Error (400-499): Indicate that the request was invalid or failed due to an error on the client side.
 *  * Server Error (500-599): Indicate that the server encountered an unexpected condition that prevented it from fulfilling the request.
 *
 * This class provides constants for commonly used HTTP response codes and exception messages
 * to aid in error handling and interpretation of network responses.
 */
object ApiResponseCode {
    /** The HTTP status code indicating a successful request. */
    const val SUCCESS = 200

    /** The HTTP status code indicating a bad request. */
    const val BAD_REQUEST = 400

    /** The HTTP status code indicating unauthorized access. */
    const val UNAUTHORIZED = 401

    /** The HTTP status code indicating the requested resource could not be found. */
    const val SERVER_NOT_FOUND = 404

    /** The HTTP status code indicating a request timeout. */
    const val REQUEST_TIMEOUT = 408

    /** The HTTP status code indicating that the URI is too long. */
    const val URI_TOO_LONG = 414

    /** The HTTP status code indicating an internal server error. */
    const val INTERNAL_SERVER_ERROR = 500

    /** The HTTP status code indicating a bad gateway. */
    const val BAD_GATEWAY = 502

    /** The HTTP status code indicating that the service is unavailable. */
    const val SERVICE_UNAVAILABLE = 503

    /** The HTTP status code indicating a gateway timeout. */
    const val GATEWAY_TIMEOUT = 504

    /** A string constant representing the exception message for IOException network errors. */
    const val IOEXCEPTION = "IOException"

    /** A string constant representing the generic exception message. */
    const val EXCEPTION = "Exception"

    /** A string constant representing the exception message for timeout exceptions. */
    const val TIMEOUT_EXCEPTION = "Timeout"
}