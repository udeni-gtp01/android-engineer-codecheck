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