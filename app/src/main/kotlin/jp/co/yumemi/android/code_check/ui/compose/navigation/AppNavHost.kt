package jp.co.yumemi.android.code_check.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jp.co.yumemi.android.code_check.ui.compose.HomeScreen
import jp.co.yumemi.android.code_check.ui.compose.PreviewScreen
import jp.co.yumemi.android.code_check.viewModel.HomeSharedViewModel

/**
 * Composable function responsible for hosting the navigation flow of the app.
 *
 * @param navController The [NavHostController] used for navigation between screens.
 * @param modifier Additional modifier to be applied to the [NavHost].
 */
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    // Create an instance of the ViewModel
    val viewModel: HomeSharedViewModel = hiltViewModel()

    // Define the navigation flow using a NavHost
    NavHost(navController = navController, startDestination = GithubRepositoryList.route) {
        composable(route = GithubRepositoryList.route) {
            HomeScreen(
                homeSharedViewModel = viewModel,
                onRepositoryItemClicked = {
                    navController.navigateSingleTopTo(GithubRepositoryPreview.route)
                },
                modifier = modifier,
            )
        }
        composable(route = GithubRepositoryPreview.route) {
            PreviewScreen(
                homeSharedViewModel = viewModel,
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
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // re-selecting the same item
        launchSingleTop = true
        // Restore state when re-selecting a previously selected item
        restoreState = true
    }

