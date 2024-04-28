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
 * A ViewModel class responsible for fetching and exposing information about a specific GitHub repository.
 * This ViewModel retrieves data from the `localGitHubDatabaseRepository` based on a repository ID
 * passed as an argument from the [SavedStateHandle].
 *The retrieved information is exposed as a Flow of `GitHubResponse`, which can represent
 * either success with the repository details or an error if the repository is not found
 * or if an exception occurs during the retrieval process or null if the retrieved argument is null.
 *
 * @param savedStateHandle Handle to access arguments passed to the ViewModel.
 * @param localGitHubDatabaseRepository Repository providing access to local GitHub data.
 */
@HiltViewModel
class GitHubRepositoryInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    localGitHubDatabaseRepository: LocalGitHubDatabaseRepository,
    logger: Logger
) : ViewModel() {
    // Logging tag for this class
    private val TAG = this.javaClass.simpleName

    /**
     * Retrieves information about the selected GitHub repository as a Flow of [GitHubResponse].
     * This Flow emits a [GitHubResponse] containing either:
     *  - Success: The fetched [LocalGitHubRepository] data.
     *  - Error: Information about an encountered error during data retrieval.
     *
     * If the [ARGUMENT_GITHUB_REPO_ID] argument is missing or invalid,an [IllegalStateException]
     * occurs during the retrieval process and is caught, logged and the repoInfo Flow is set to null.
     */
    val gitHubRepositoryInfo: Flow<GitHubResponse<LocalGitHubRepository?>>? = try {
        localGitHubDatabaseRepository.getSelectedGitHubRepositoryFromDatabase(
            checkNotNull(
                savedStateHandle[ARGUMENT_GITHUB_REPO_ID]
            )
        )
    } catch (ex: Exception) {
        logger.error(TAG, ex.message ?: "Invalid argument", ex)
        null
    }
}