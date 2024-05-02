package jp.co.yumemi.android.code_check.ui.compose.navigation

import jp.co.yumemi.android.code_check.constant.NavigationArgument.DESTINATION_GITHUB_REPO_INFO
import jp.co.yumemi.android.code_check.constant.NavigationArgument.DESTINATION_HOME
import jp.co.yumemi.android.code_check.constant.NavigationArgument.DESTINATION_MY_SAVED_LIST

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
 * An object representing the GitHub Repository info destination.
 */
object GitHubRepositoryInfoDestination : AppDestination {
    /**
     * The route to the GitHub repository info screen.
     */
    override val route = DESTINATION_GITHUB_REPO_INFO
}

/**
 * An object representing the user's saved list destination.
 */
object MySavedListDestination : AppDestination {
    /**
     * The route to the user's saved list screen.
     */
    override val route = DESTINATION_MY_SAVED_LIST
}