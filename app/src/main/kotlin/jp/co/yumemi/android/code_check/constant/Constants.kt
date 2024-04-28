package jp.co.yumemi.android.code_check.constant

/**
 * This object provides constant values used throughout the application
 * for interacting with the GitHub API. These constants ensure consistency and
 * avoid typos when making API calls.
 */
object Constant {

    /**
     * The base URL for the GitHub API. This URL is prepended to all API endpoints.
     */
    const val BASE_URL = "https://api.github.com/search/"

    /**
     * The specific endpoint within the GitHub API used for retrieving repositories.
     * This value is appended to the base URL to construct the complete request URL
     * for searching repositories.
     */
    const val ENDPOINT_GITHUB_REPOSITORIES = "repositories"

    /**
     * The header value used to specify the accepted response format in the GitHub API.
     * Setting this header ensures the API returns data in JSON format, which is
     * typically easier to parse within the application.
     */
    const val HEADER_TYPE = "application/vnd.github.v3+json"
    const val ROOM_GITHUB_REPO_TABLE_NAME = "github_repo_table"
}