package jp.co.yumemi.android.code_check.ui.compose.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import jp.co.yumemi.android.code_check.constant.NavigationArgument.ARGUMENT_GITHUB_REPO_ID
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
     * The route to the GitHub repository preview screen.
     */
    override val route = DESTINATION_GITHUB_REPO_INFO

    /**
     * The route with a placeholder for the `repoIdArg`.
     */
    val routeWithArgs = "$route/{$ARGUMENT_GITHUB_REPO_ID}"

    /** The list of navigation arguments for this destination. */
    val arguments = listOf(
        navArgument(ARGUMENT_GITHUB_REPO_ID) { type = NavType.LongType }
    )
}