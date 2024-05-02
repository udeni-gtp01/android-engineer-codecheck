package jp.co.yumemi.android.code_check.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jp.co.yumemi.android.code_check.ui.compose.GitHubRepositoryInfoScreen
import jp.co.yumemi.android.code_check.ui.compose.HomeScreen
import jp.co.yumemi.android.code_check.ui.compose.MySavedListScreen

/**
 * Composable function responsible for hosting the navigation flow of the app.
 *
 * @param navController The [NavHostController] used for navigation between screens.
 * @param modifier Additional modifier to be applied to the [NavHost].
 */
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    // Define the navigation flow using a NavHost
    NavHost(navController = navController, startDestination = HomeDestination.route) {
        composable(route = HomeDestination.route) {
            /**
             * The Home screen composable. This screen displays a list of GitHub repositories
             * and allows users to click on them to navigate to the github repository info screen.
             *
             * @param navigateToGitHubRepositoryInfo A callback function invoked when a user clicks on a repository item.
             * @param modifier Additional modifier to be applied to the composable.
             */
            HomeScreen(
                navigateToGitHubRepositoryInfo = {
                    navController.navigateSingleTopTo(
                        GitHubRepositoryInfoDestination.route
                    )
                },
                onMySavedListButtonClick = {
                    navController.navigateSingleTopTo(
                        MySavedListDestination.route
                    )
                },
                modifier = modifier,
            )
        }
        composable(
            route = GitHubRepositoryInfoDestination.route,
        ) {
            /**
             * The composable screen for displaying detailed information about a specific GitHub repository.
             * This screen is accessible by navigating to the route `${GitHubRepositoryInfoDestination.route}/`
             *
             * @param modifier Additional modifier to be applied to the composable.
             */
            GitHubRepositoryInfoScreen(
                modifier = modifier
            )
        }
        composable(
            route = MySavedListDestination.route,
        ) {
            /**
             * The composable screen for displaying GitHub repositories saved in the database for later reference.
             * This screen is accessible by navigating to the route `${MySavedListDestination.route}/`
             *
             * @param modifier Additional modifier to be applied to the composable.
             */
            MySavedListScreen(
                navigateToGitHubRepositoryInfo = {
                    navController.navigateSingleTopTo(
                        GitHubRepositoryInfoDestination.route
                    )
                },
                modifier = modifier
            )
        }
    }
}

/**
 * Extension function to navigate with a single top behavior. It navigates to the specified route
 * while ensuring that multiple copies of the same destination are not added to the back stack.
 *
 * @param route The route to navigate to.
 */
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = false
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }