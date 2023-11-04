package jp.co.yumemi.android.code_check.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jp.co.yumemi.android.code_check.ui.view.HomeScreen
import jp.co.yumemi.android.code_check.ui.view.PreviewScreen
import jp.co.yumemi.android.code_check.view_model.GithubRepoViewModel

/**
 * Composable function responsible for hosting the navigation flow of the app.
 *
 * @param navController The [NavHostController] used for navigation between screens.
 */
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {

    val viewModel: GithubRepoViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = GithubRepositoryList.route) {
        composable(route = GithubRepositoryList.route) {
            HomeScreen(
                githubRepoViewModel = viewModel,
                onRepositoryItemClicked = {
                    navController.navigateSingleTopTo(GithubRepositoryPreview.route)
                },
                modifier = modifier,
            )
        }
        composable(route = GithubRepositoryPreview.route) {
            PreviewScreen(
                githubRepoViewModel = viewModel,
                modifier = modifier
            )
        }
    }
}

// Extension function to navigate with single top behavior
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

