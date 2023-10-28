package jp.co.yumemi.android.code_check.navigation

interface AppDestination {
    /**
     * The route associated with the destination.
     */
    val route: String
}

object GithubRepositoryList : AppDestination {
    override val route = "repo_list"
}
