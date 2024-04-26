package jp.co.yumemi.android.code_check.ui.compose.navigation

/**
 * An interface that represents a navigation destination in the app.
 */
interface AppDestination {
    /**
     * The route associated with the destination.
     */
    val route: String
}

/**
 * An object representing the GitHub Repository List destination.
 */
object GithubRepositoryList : AppDestination {
    override val route = "repo_list"
}

/**
 * An object representing the GitHub Repository Preview destination.
 */
object GithubRepositoryPreview : AppDestination {
    override val route = "repo_preview"
}