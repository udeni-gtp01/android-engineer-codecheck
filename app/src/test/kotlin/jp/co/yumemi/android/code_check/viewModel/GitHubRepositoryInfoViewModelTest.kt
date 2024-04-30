package jp.co.yumemi.android.code_check.viewModel

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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
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
    private lateinit var localGitHubDatabaseRepository: LocalGitHubDatabaseRepository

    /**
     * A mock instance of `LocalGitHubRepository` representing a test repository.
     * This object is used to provide expected data for the test cases.
     */
    private val testGitHubRepository = LocalGitHubRepository(
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
     * The ViewModel emits a success response containing the corresponding repository data
     * retrieved from the `localGitHubDatabaseRepository`.
     */
    @Test
    fun `getSelectedGitHubRepositoryFromDatabase should emit success response if no error`() =
        runTest {
            `when`(
                localGitHubDatabaseRepository.getSelectedGitHubRepositoryFromDatabase()
            ).thenReturn(flowOf(GitHubResponse.Success(testGitHubRepository)))

            viewModel = GitHubRepositoryInfoViewModel(
                localGitHubDatabaseRepository = localGitHubDatabaseRepository
            )

            val gitHubRepositoryInfoFlow: Flow<GitHubResponse<LocalGitHubRepository?>> =
                viewModel.gitHubRepositoryInfo

            assertNotNull(gitHubRepositoryInfoFlow)
            // If gitHubRepositoryInfoFlow is not null, collect its values
            gitHubRepositoryInfoFlow.collect { gitHubRepositoryInfo ->
                // Verify that the gitHubRepositoryInfo value matches the expected value
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
            `when`(
                localGitHubDatabaseRepository.getSelectedGitHubRepositoryFromDatabase()
            ).thenReturn(flowOf(GitHubResponse.Error(errorMessage)))

            viewModel = GitHubRepositoryInfoViewModel(
                localGitHubDatabaseRepository = localGitHubDatabaseRepository
            )

            val gitHubRepositoryInfoFlow: Flow<GitHubResponse<LocalGitHubRepository?>> =
                viewModel.gitHubRepositoryInfo

            assertNotNull(gitHubRepositoryInfoFlow)
            // If gitHubRepositoryInfoFlow is not null, collect its values
            gitHubRepositoryInfoFlow.collect { gitHubRepositoryInfo ->
                // Verify that the gitHubRepositoryInfo value matches the expected value
                assertTrue(gitHubRepositoryInfo is GitHubResponse.Error)
                assertEquals(GitHubResponse.Error(errorMessage), gitHubRepositoryInfo)
            }
        }

    /**
     * This test verifies that if no github repository is saved in room database
     * the ViewModel emits a success response containing null `LocalGitHubRepository`.
     */
    @Test()
    fun `getSelectedGitHubRepositoryFromDatabase should emit null if github repository is not found`() =
        runTest {
            `when`(
                localGitHubDatabaseRepository.getSelectedGitHubRepositoryFromDatabase()
            ).thenReturn(flowOf(GitHubResponse.Success(null)))

            viewModel = GitHubRepositoryInfoViewModel(
                localGitHubDatabaseRepository = localGitHubDatabaseRepository
            )

            val gitHubRepositoryInfoFlow: Flow<GitHubResponse<LocalGitHubRepository?>> =
                viewModel.gitHubRepositoryInfo

            assertNotNull(gitHubRepositoryInfoFlow)

            // If gitHubRepositoryInfoFlow is not null, collect its values
            gitHubRepositoryInfoFlow.collect { gitHubRepositoryInfo ->
                // Verify that the gitHubRepositoryInfo value matches the expected value
                assertTrue(gitHubRepositoryInfo is GitHubResponse.Success)
                assertEquals(GitHubResponse.Success(null), gitHubRepositoryInfo)
            }
        }
}