package jp.co.yumemi.android.code_check.viewModel

import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.model.toSavedGitHubRepository
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
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
    /**
     * TestDispatcher to run coroutines in tests.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @ObsoleteCoroutinesApi
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: GitHubRepositoryInfoViewModel

    @Mock
    private lateinit var localGitHubDatabaseRepository: LocalGitHubDatabaseRepository

    @Mock
    private lateinit var logger: Logger

    /**
     * A mock instance of `LocalGitHubRepository` representing a test repository.
     * This object is used to provide expected data for the test cases.
     */
    private val testGitHubRepository = LocalGitHubRepository(
        id = 123,
        forksCount = 50,
        language = "Kotlin",
        name = "My Awesome Project",
        openIssuesCount = 2,
        stargazersCount = 100,
        watchersCount = 75,
        htmlUrl = "https://github.com/user/my-awesome-project",
        ownerLogin = "user",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/user",
        isSaved = true
    )

    /**
     * Setup method to initialize the objects before each test case.
     */
    @OptIn(ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = GitHubRepositoryInfoViewModel(
            localGitHubDatabaseRepository = localGitHubDatabaseRepository,
            logger = logger
        )
    }

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
                localGitHubDatabaseRepository = localGitHubDatabaseRepository,
                logger = logger
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
                localGitHubDatabaseRepository = localGitHubDatabaseRepository,
                logger = logger
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
                localGitHubDatabaseRepository = localGitHubDatabaseRepository,
                logger = logger
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

    /**
     * Test case to verify that addGitHubRepositoryToMySavedList executes onSuccess callback correctly on success.
     */
    @Test
    fun `addGitHubRepositoryToMySavedList executes onSuccess callback correctly on success`() =
        runTest {
            val testSavedGitHubRepository =
                testGitHubRepository.toSavedGitHubRepository(isSaved = true)
            `when`(
                localGitHubDatabaseRepository.addGitHubRepositoryToMySavedList(
                    testSavedGitHubRepository
                )
            )
                .thenReturn(MutableStateFlow(GitHubResponse.Success(true)))

            // Initialize variables to track the callbacks
            var onSuccessCalled = false
            var onErrorCalled = false
            var onLoadingCalled = false

            viewModel.addGitHubRepositoryToMySavedList(
                localGitHubRepository = testGitHubRepository,
                onSuccess = { onSuccessCalled = true },
                onError = { onErrorCalled = true },
                onLoading = { onLoadingCalled = true }
            )

            // Ensure that onSuccess callback was invoked
            assertTrue(onSuccessCalled)

            // Ensure that onError and onLoading callbacks were not invoked
            assertFalse(onErrorCalled)
            assertFalse(onLoadingCalled)
        }

    /**
     * Test case to verify that addGitHubRepositoryToMySavedList executes onError callback correctly on error.
     */
    @Test
    fun `addGitHubRepositoryToMySavedList executes onError callback correctly on error`() =
        runTest {
            val testSavedGitHubRepository =
                testGitHubRepository.toSavedGitHubRepository(isSaved = true)
            val errorMessage = "Failed to addGitHubRepositoryToMySavedList"

            `when`(
                localGitHubDatabaseRepository.addGitHubRepositoryToMySavedList(
                    testSavedGitHubRepository
                )
            )
                .thenReturn(MutableStateFlow(GitHubResponse.Error(errorMessage)))

            // Initialize variables to track the callbacks
            var onSuccessCalled = false
            var onErrorCalled = false
            var onLoadingCalled = false

            viewModel.addGitHubRepositoryToMySavedList(
                localGitHubRepository = testGitHubRepository,
                onSuccess = { onSuccessCalled = true },
                onError = { onErrorCalled = true },
                onLoading = { onLoadingCalled = true }
            )

            // Ensure that onError callback was invoked
            assertTrue(onErrorCalled)

            // Ensure that onSuccess and onLoading callbacks were not invoked
            assertFalse(onSuccessCalled)
            assertFalse(onLoadingCalled)
        }

    /**
     * Test case to verify that addGitHubRepositoryToMySavedList executes onLoading callback correctly while waiting for response.
     */
    @Test
    fun `addGitHubRepositoryToMySavedList executes onLoading callback correctly while waiting for response`() =
        runTest {
            val testSavedGitHubRepository =
                testGitHubRepository.toSavedGitHubRepository(isSaved = true)

            `when`(
                localGitHubDatabaseRepository.addGitHubRepositoryToMySavedList(
                    testSavedGitHubRepository
                )
            )
                .thenReturn(MutableStateFlow(GitHubResponse.Loading))

            // Initialize variables to track the callbacks
            var onSuccessCalled = false
            var onErrorCalled = false
            var onLoadingCalled = false

            viewModel.addGitHubRepositoryToMySavedList(
                localGitHubRepository = testGitHubRepository,
                onSuccess = { onSuccessCalled = true },
                onError = { onErrorCalled = true },
                onLoading = { onLoadingCalled = true }
            )

            // Ensure that onLoading callback was invoked
            assertTrue(onLoadingCalled)

            // Ensure that onSuccess and onError callbacks were not invoked
            assertFalse(onSuccessCalled)
            assertFalse(onErrorCalled)
        }

    /**
     * Test case to verify that removeGitHubRepositoryFromMySavedList executes onSuccess callback correctly on success.
     */
    @Test
    fun `removeGitHubRepositoryFromMySavedList executes onSuccess callback correctly on success`() =
        runTest {
            val testSavedGitHubRepository =
                testGitHubRepository.toSavedGitHubRepository(isSaved = false)
            `when`(
                localGitHubDatabaseRepository.removeGitHubRepositoryFromMySavedList(
                    testSavedGitHubRepository
                )
            )
                .thenReturn(MutableStateFlow(GitHubResponse.Success(true)))

            // Initialize variables to track the callbacks
            var onSuccessCalled = false
            var onErrorCalled = false
            var onLoadingCalled = false

            viewModel.removeGitHubRepositoryFromMySavedList(
                localGitHubRepository = testGitHubRepository,
                onSuccess = { onSuccessCalled = true },
                onError = { onErrorCalled = true },
                onLoading = { onLoadingCalled = true }
            )

            // Ensure that onSuccess callback was invoked
            assertTrue(onSuccessCalled)

            // Ensure that onError and onLoading callbacks were not invoked
            assertFalse(onErrorCalled)
            assertFalse(onLoadingCalled)
        }

    /**
     * Test case to verify that removeGitHubRepositoryFromMySavedList executes onError callback correctly on error.
     */
    @Test
    fun `removeGitHubRepositoryFromMySavedList executes onError callback correctly on error`() =
        runTest {
            val testSavedGitHubRepository =
                testGitHubRepository.toSavedGitHubRepository(isSaved = false)
            val errorMessage = "Failed to addGitHubRepositoryToMySavedList"

            `when`(
                localGitHubDatabaseRepository.removeGitHubRepositoryFromMySavedList(
                    testSavedGitHubRepository
                )
            )
                .thenReturn(MutableStateFlow(GitHubResponse.Error(errorMessage)))

            // Initialize variables to track the callbacks
            var onSuccessCalled = false
            var onErrorCalled = false
            var onLoadingCalled = false

            viewModel.removeGitHubRepositoryFromMySavedList(
                localGitHubRepository = testGitHubRepository,
                onSuccess = { onSuccessCalled = true },
                onError = { onErrorCalled = true },
                onLoading = { onLoadingCalled = true }
            )

            // Ensure that onError callback was invoked
            assertTrue(onErrorCalled)

            // Ensure that onSuccess and onLoading callbacks were not invoked
            assertFalse(onSuccessCalled)
            assertFalse(onLoadingCalled)
        }

    /**
     * Test case to verify that removeGitHubRepositoryFromMySavedList executes onLoading callback correctly while waiting for response.
     */
    @Test
    fun `removeGitHubRepositoryFromMySavedList executes onLoading callback correctly while waiting for response`() =
        runTest {
            val testSavedGitHubRepository =
                testGitHubRepository.toSavedGitHubRepository(isSaved = false)

            `when`(
                localGitHubDatabaseRepository.removeGitHubRepositoryFromMySavedList(
                    testSavedGitHubRepository
                )
            )
                .thenReturn(MutableStateFlow(GitHubResponse.Loading))

            // Initialize variables to track the callbacks
            var onSuccessCalled = false
            var onErrorCalled = false
            var onLoadingCalled = false

            viewModel.removeGitHubRepositoryFromMySavedList(
                localGitHubRepository = testGitHubRepository,
                onSuccess = { onSuccessCalled = true },
                onError = { onErrorCalled = true },
                onLoading = { onLoadingCalled = true }
            )

            // Ensure that onLoading callback was invoked
            assertTrue(onLoadingCalled)

            // Ensure that onSuccess and onError callbacks were not invoked
            assertFalse(onSuccessCalled)
            assertFalse(onErrorCalled)
        }
}