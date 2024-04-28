package jp.co.yumemi.android.code_check.viewModel

import androidx.lifecycle.SavedStateHandle
import jp.co.yumemi.android.code_check.constant.NavigationArgument.ARGUMENT_GITHUB_REPO_ID
import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit test class for `GitHubRepositoryInfoViewModel`. This class verifies the
 * behavior of the ViewModel under various scenarios.
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GitHubRepositoryInfoViewModelTest {
    private lateinit var viewModel: GitHubRepositoryInfoViewModel

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    @Mock
    private lateinit var localGitHubDatabaseRepository: LocalGitHubDatabaseRepository

    @Mock
    private lateinit var logger: Logger

    /**
     * A mock instance of `LocalGitHubRepository` representing a test repository.
     * This object is used to provide expected data for the test cases.
     */
    private val testGitHubRepository = LocalGitHubRepository(
        id = 12345,
        forksCount = 50,
        language = "Kotlin",
        name = "My Awesome Project",
        openIssuesCount = 2,
        stargazersCount = 100,
        watchersCount = 75,
        htmlUrl = "https://github.com/user/my-awesome-project",
        ownerLogin = "user",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/user"
    )

    /**
     * This test verifies that when a valid repository ID is provided as an argument,
     * the ViewModel emits a success response containing the corresponding repository data
     * retrieved from the `localGitHubDatabaseRepository`.
     */
    @Test
    fun `valid github repository id argument should emit success response`() = runTest {
        `when`(savedStateHandle.get<Long>(ARGUMENT_GITHUB_REPO_ID))
            .thenReturn(testGitHubRepository.id)

        `when`(
            localGitHubDatabaseRepository.getSelectedGitHubRepositoryFromDatabase(
                testGitHubRepository.id
            )
        ).thenReturn(flowOf(GitHubResponse.Success(testGitHubRepository)))

        viewModel = GitHubRepositoryInfoViewModel(
            savedStateHandle = savedStateHandle,
            localGitHubDatabaseRepository = localGitHubDatabaseRepository,
            logger = logger
        )

        val gitHubRepositoryInfoFlow: Flow<GitHubResponse<LocalGitHubRepository?>>? =
            viewModel.gitHubRepositoryInfo

        assertNotNull(gitHubRepositoryInfoFlow)
        // If repoInfoFlow is not null, collect its values
        gitHubRepositoryInfoFlow?.collect { gitHubRepositoryInfo ->
            // Verify that the repoInfo value matches the expected value
            assertTrue(gitHubRepositoryInfo is GitHubResponse.Success)
            assertEquals(GitHubResponse.Success(testGitHubRepository), gitHubRepositoryInfo)
        }
    }

    /**
     * This test verifies that if an error occurs while fetching data from the
     * `localGitHubDatabaseRepository`, the ViewModel emits an error response
     * containing the error message.
     */
    @Test
    fun `error from getSelectedGitHubRepositoryFromDatabase should emit error response`() =
        runTest {
            val errorMessage = "Error"
            `when`(savedStateHandle.get<Long>(ARGUMENT_GITHUB_REPO_ID))
                .thenReturn(testGitHubRepository.id)

            `when`(
                localGitHubDatabaseRepository.getSelectedGitHubRepositoryFromDatabase(
                    testGitHubRepository.id
                )
            ).thenReturn(flowOf(GitHubResponse.Error(errorMessage)))

            viewModel = GitHubRepositoryInfoViewModel(
                savedStateHandle = savedStateHandle,
                localGitHubDatabaseRepository = localGitHubDatabaseRepository,
                logger = logger
            )

            val gitHubRepositoryInfoFlow: Flow<GitHubResponse<LocalGitHubRepository?>>? =
                viewModel.gitHubRepositoryInfo

            assertNotNull(gitHubRepositoryInfoFlow)
            // If repoInfoFlow is not null, collect its values
            gitHubRepositoryInfoFlow?.collect { gitHubRepositoryInfo ->
                // Verify that the repoInfo value matches the expected value
                assertTrue(gitHubRepositoryInfo is GitHubResponse.Error)
                assertEquals(GitHubResponse.Error(errorMessage), gitHubRepositoryInfo)
            }
        }

    /**
     * This test verifies that if the `ARGUMENT_GITHUB_REPO_ID` argument is missing
     * from the `savedStateHandle`, the ViewModel sets the `gitHubRepositoryInfo` flow to null.
     */
    @Test()
    fun `null github repository id argument should set null flow`() = runTest {
        `when`(savedStateHandle.get<Long>(ARGUMENT_GITHUB_REPO_ID)).thenReturn(null)
        viewModel = GitHubRepositoryInfoViewModel(
            savedStateHandle = savedStateHandle,
            localGitHubDatabaseRepository = localGitHubDatabaseRepository,
            logger = logger
        )
        val gitHubRepositoryInfoFlow: Flow<GitHubResponse<LocalGitHubRepository?>>? =
            viewModel.gitHubRepositoryInfo

        assertNull(gitHubRepositoryInfoFlow)
    }
}