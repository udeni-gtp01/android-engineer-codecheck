package jp.co.yumemi.android.code_check.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface AppDestination {
    /**
     * The route associated with the destination.
     */
    val route: String
}

object GithubRepositoryList : AppDestination {
    override val route = "repo_list"
}

object GithubRepositoryPreview : AppDestination {
    override val route = "repo_preview"
}

//object GithubRepositoryPreview : AppDestination {
//    override val route = "repo_preview"
//    const val repositoryIdArg = "repository_item"
//    val routeWithArgs = "$route/{$repositoryIdArg}"
//    val arguments = listOf(
//        navArgument(repositoryIdArg) { type = NavType.StringType }
//    )
//}