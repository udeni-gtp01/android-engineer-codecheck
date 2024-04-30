package jp.co.yumemi.android.code_check.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.constant.NavigationArgument.ARGUMENT_GITHUB_REPO_ID
import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A ViewModel class responsible for fetching and exposing information about a selected GitHub repository.
 * This ViewModel retrieves the selected GitHub repository from the [LocalGitHubRepository] table in Room database.
 * The [LocalGitHubRepository] table contains only one row to store the most recently selected
 * github repository and that has fixed value (`1`) as the primary key.
 *
 * The retrieved information is exposed as a Flow of `GitHubResponse`, which can represent
 * either success with the repository details or an error if the repository is not found
 * or if an exception occurs during the retrieval process or null if the retrieved argument is null.
 *
 * @param localGitHubDatabaseRepository Repository providing access to local GitHub data.
 */
@HiltViewModel
class GitHubRepositoryInfoViewModel @Inject constructor(
    localGitHubDatabaseRepository: LocalGitHubDatabaseRepository,
) : ViewModel() {
    /**
     * Retrieves information about the selected GitHub repository as a Flow of [GitHubResponse].
     * This Flow emits a [GitHubResponse] containing either:
     *  - Success: The fetched [LocalGitHubRepository] data.
     *  - Error: Information about an encountered error during data retrieval.
     */
    val gitHubRepositoryInfo: Flow<GitHubResponse<LocalGitHubRepository?>> =
        localGitHubDatabaseRepository.getSelectedGitHubRepositoryFromDatabase()
}