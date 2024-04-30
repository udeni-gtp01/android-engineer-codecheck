package jp.co.yumemi.android.code_check.constant

/**
 * This object provides constant values used throughout the application
 * for interacting with the GitHub API. Using constants ensures consistency, reduces the risk of typos,
 * and improves code readability.
 */
object ApiEndpoint {

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
}

/**
 * This object holds constant values related to the database used by the application.
 */
object DatabaseConstant {
    /**
     * Constant string representing the name of the table in the database that stores information
     * about selected GitHub repository to view info.
     */
    const val ROOM_GITHUB_REPO_DB_NAME = "github_repo_db"

    /**
     * Constant string representing the name of the table in the database that stores information
     * about selected GitHub repository to view info.
     */
    const val ROOM_GITHUB_REPO_TABLE_NAME = "github_repo_table"
}

/**
 * This object provides constant values used for arguments passed between navigation events.
 * Using constants for argument names improves code readability, reduces typos, and ensures
 * consistency across the application.
 */
object NavigationArgument {
    /**
     * The key used to access the GitHub repository ID argument passed during navigation.
     */
    const val ARGUMENT_GITHUB_REPO_ID = "gitHubRepoId"

    /**
     * The constant representing app home destination.
     */
    const val DESTINATION_HOME = "home"

    /**
     * The constant representing github repository info destination.
     */
    const val DESTINATION_GITHUB_REPO_INFO = "repo_info"
}

/**
 * This object provides HTTP response codes from GitHub REST API and exception messages
 * to aid in error handling and interpretation of network responses.
 */
object ResponseCode {
    /** HTTP response status codes for 'Validation failed, or the endpoint has been spammed' error response */
    const val STATUS_CODE_422 = "422"

    /** HTTP response status codes for 'Service unavailable' error response */
    const val STATUS_CODE_503 = "503"

    /** A string constant representing the exception message for IOException network errors. */
    const val IOEXCEPTION = "IOException"

    /** A string constant representing the generic exception message. */
    const val EXCEPTION = "Exception"

    /** A string constant representing the exception message for timeout exceptions. */
    const val TIMEOUT_EXCEPTION = "Timeout"
}