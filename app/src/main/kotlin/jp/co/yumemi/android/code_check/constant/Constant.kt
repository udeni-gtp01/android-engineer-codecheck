package jp.co.yumemi.android.code_check.constant

/**
 * Constants used in the application.
 */
object Constant {
    /**
     * Base URL for the GitHub API.
     */
    const val BASE_URL = "https://api.github.com/search/"

    /**
     * Endpoint for extracting repositories in the GitHub API.
     */
    const val ENDPOINT_REPOSITORY = "repositories"

    /**
     * Header value for specifying the accepted response format in the GitHub API.
     */
    const val HEADER_1 = "Accept: application/vnd.github.v3+json"
}