package jp.co.yumemi.android.code_check.ui.compose.navigation

import jp.co.yumemi.android.code_check.constant.NavigationArgument.DESTINATION_GITHUB_REPO_INFO
import jp.co.yumemi.android.code_check.constant.NavigationArgument.DESTINATION_HOME

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
object HomeDestination : AppDestination {
    override val route = DESTINATION_HOME
}

/**
 * An object representing the GitHub Repository Preview destination.
 */
object GitHubRepositoryInfoDestination : AppDestination {
    /**
     * The route to the GitHub repository info screen.
     */
    override val route = DESTINATION_GITHUB_REPO_INFO
}