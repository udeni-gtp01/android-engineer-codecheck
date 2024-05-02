package jp.co.yumemi.android.code_check.viewModel

import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.SavedGitHubRepository
import jp.co.yumemi.android.code_check.model.toLocalGitHubRepository
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit test class for `MySavedListViewModel`. This class verifies the
 * behavior of the ViewModel under various scenarios.
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MySavedListViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @ObsoleteCoroutinesApi
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: MySavedListViewModel

    @Mock
    private lateinit var localGitHubDatabaseRepository: LocalGitHubDatabaseRepository

    @Mock
    private lateinit var logger: Logger

    /**
     * A mock instance of `SavedGitHubRepository` representing a test repository.
     * This object is used to provide expected data for the test cases.
     */
    private val testGitHubRepository = SavedGitHubRepository(
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
    @OptIn(ObsoleteCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = MySavedListViewModel(
            localGitHubDatabaseRepository = localGitHubDatabaseRepository,
            logger = logger
        )
    }

    /**
     * The ViewModel emits a success response containing the corresponding saved repository data
     * retrieved from the `localGitHubDatabaseRepository`.
     */
    @Test
    fun `getMySavedList should emit success response if no error`() =
        runTest {
            // List represents GitHub repositories saved in user's list
            val savedRepoList = listOf(testGitHubRepository)

            `when`(localGitHubDatabaseRepository.getMySavedList())
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Success(savedRepoList))
                )

            viewModel.getMySavedList()

            // Verify that mySavedListState is updated correctly
            assertTrue(viewModel.mySavedListState.value is GitHubResponse.Success)
            assertTrue((viewModel.mySavedListState.value as GitHubResponse.Success).data == savedRepoList)

        }

    /**
     * The ViewModel emits a error response containing the corresponding error
     * retrieved from the `localGitHubDatabaseRepository`.
     */
    @Test
    fun `getMySavedList should emit error response if on error`() =
        runTest {
            val errorMessage = "Failed searchGitHubRepositories"

            `when`(localGitHubDatabaseRepository.getMySavedList())
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Error(errorMessage))
                )

            viewModel.getMySavedList()

            // Verify that mySavedListState is updated correctly
            assertTrue(viewModel.mySavedListState.value is GitHubResponse.Error)
            assertTrue((viewModel.mySavedListState.value as GitHubResponse.Error).error == errorMessage)

        }

    /**
     * The ViewModel emits a success response containing the corresponding success message
     * retrieved from the `localGitHubDatabaseRepository`.
     */
    @Test
    fun `saveSelectedGitHubRepositoryInDatabase should emit success response if no error`() =
        runTest {
            val testLocalRepo = testGitHubRepository.toLocalGitHubRepository()

            Mockito.`when`(
                localGitHubDatabaseRepository.saveSelectedGitHubRepositoryInDatabase(
                    testLocalRepo
                )
            )
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Success(true))
                )

            viewModel.saveSelectedGitHubRepositoryInDatabase(testGitHubRepository)

            // Verify that isSelectedGitHubRepositorySavedState is updated correctly
            Assert.assertTrue(viewModel.isSelectedGitHubRepositorySavedState.value is GitHubResponse.Success)
            Assert.assertTrue((viewModel.isSelectedGitHubRepositorySavedState.value as GitHubResponse.Success).data)
        }

    /**
     * The ViewModel emits a error response containing the corresponding error message
     * retrieved from the `localGitHubDatabaseRepository`.
     */
    @Test
    fun `saveSelectedGitHubRepositoryInDatabase should emit error response if on error`() =
        runTest {
            var testLocalRepo = testGitHubRepository.toLocalGitHubRepository()
            val errorMessage = "Failed searchGitHubRepositories"

            `when`(
                localGitHubDatabaseRepository.saveSelectedGitHubRepositoryInDatabase(
                    testLocalRepo
                )
            )
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Error(errorMessage))
                )

            viewModel.saveSelectedGitHubRepositoryInDatabase(testGitHubRepository)

            // Verify that isSelectedGitHubRepositorySavedState is updated correctly
            assertTrue(viewModel.isSelectedGitHubRepositorySavedState.value is GitHubResponse.Error)
            assertTrue((viewModel.isSelectedGitHubRepositorySavedState.value as GitHubResponse.Error).error == errorMessage)
        }
}