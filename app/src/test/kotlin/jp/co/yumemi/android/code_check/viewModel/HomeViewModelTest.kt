package jp.co.yumemi.android.code_check.viewModel

import jp.co.yumemi.android.code_check.logger.Logger
import jp.co.yumemi.android.code_check.model.GitHubRepository
import jp.co.yumemi.android.code_check.model.GitHubRepositoryList
import jp.co.yumemi.android.code_check.model.GitHubRepositoryOwner
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.SavedGitHubRepository
import jp.co.yumemi.android.code_check.model.toLocalGitHubRepository
import jp.co.yumemi.android.code_check.model.toSavedGitHubRepository
import jp.co.yumemi.android.code_check.repository.GitHubApiRepository
import jp.co.yumemi.android.code_check.repository.LocalGitHubDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit test class for `HomeViewModel`. This class verifies the
 * behavior of the ViewModel under various scenarios.
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @ObsoleteCoroutinesApi
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var gitHubApiRepository: GitHubApiRepository

    @Mock
    private lateinit var localGitHubDatabaseRepository: LocalGitHubDatabaseRepository

    @Mock
    private lateinit var logger: Logger

    /**
     * A mock instance of `GitHubRepository` representing test repositories.
     */
    private val testGitHubRepository1 = GitHubRepository(
        id = 1,
        name = "result repo 1/repo 1",
        forksCount = 10,
        language = "",
        owner = GitHubRepositoryOwner(login = "name", avatarUrl = "url"),
        stargazersCount = 10,
        watchersCount = 10,
        openIssuesCount = 10,
        htmlUrl = "html url"
    )
    private val testGitHubRepository2 = GitHubRepository(
        id = 2,
        name = "result repo 2/repo 2",
        forksCount = 20,
        language = "",
        owner = GitHubRepositoryOwner(login = "name", avatarUrl = "url"),
        stargazersCount = 20,
        watchersCount = 20,
        openIssuesCount = 20,
        htmlUrl = "html url"
    )

    /**
     * Setup method to initialize the objects before each test case.
     */
    @OptIn(ObsoleteCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = HomeViewModel(
            gitHubApiRepository = gitHubApiRepository,
            localGitHubDatabaseRepository = localGitHubDatabaseRepository,
            logger = logger
        )
    }

    @Test
    fun `initially search keyword should be empty`() {
        assertTrue(viewModel.searchKeyword.isEmpty())
    }

    @Test
    fun `provided valid keyword should search for the repository list`() =
        runTest {
            // List represents GitHub repositories searched from GitHub API
            val searchedRepoList = listOf(testGitHubRepository1, testGitHubRepository2)

            // List represents expected GitHub repositories result
            val expectedRepoList = listOf(
                testGitHubRepository1.toLocalGitHubRepository(isSaved = false),
                testGitHubRepository2.toLocalGitHubRepository(isSaved = false)
            )

            // Search keyword
            val keyword = "repo"

            `when`(localGitHubDatabaseRepository.getMySavedList())
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Success(emptyList()))
                )
            `when`(gitHubApiRepository.searchGitHubRepositories(keyword))
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Success(GitHubRepositoryList(searchedRepoList)))
                )

            viewModel.updateSearchKeyword(keyword = keyword)
            viewModel.searchGitHubRepositories()
            // Verify that gitHubSearchResultState is updated correctly
            assertTrue(viewModel.gitHubSearchResultState.value is GitHubResponse.Success)
            assertTrue((viewModel.gitHubSearchResultState.value as GitHubResponse.Success).data == expectedRepoList)
        }

    @Test
    fun `provided invalid keyword should empty the repository list`() = runTest {
        viewModel.updateSearchKeyword(keyword = "    ")
        viewModel.searchGitHubRepositories()
        assertTrue(viewModel.gitHubSearchResultState.value is GitHubResponse.Success)
        assertTrue((viewModel.gitHubSearchResultState.value as GitHubResponse.Success).data == emptyList<LocalGitHubDatabaseRepository>())
    }

    @Test
    fun `provided empty keyword should empty the repository list`() = runTest {
        viewModel.updateSearchKeyword(keyword = "")
        viewModel.searchGitHubRepositories()
        assertTrue(viewModel.gitHubSearchResultState.value is GitHubResponse.Success)
        assertTrue((viewModel.gitHubSearchResultState.value as GitHubResponse.Success).data == emptyList<LocalGitHubDatabaseRepository>())
    }

    @Test
    fun `clearSearchKeyword should clear the keyword and empty the repository list`() = runTest {
        viewModel.clearSearchKeyword()
        assertTrue(viewModel.searchKeyword.isEmpty())
        assertTrue(viewModel.gitHubSearchResultState.value is GitHubResponse.Success)
        assertTrue((viewModel.gitHubSearchResultState.value as GitHubResponse.Success).data == emptyList<LocalGitHubDatabaseRepository>())
    }

    /**
     * The ViewModel emits a success response containing the corresponding repository data
     * retrieved from the `gitHubApiRepository`.
     */
    @Test
    fun `searchGitHubRepositories should emit success response if no error`() =
        runTest {
            // List represents GitHub repositories saved in user's saved list
            val savedRepoList = emptyList<SavedGitHubRepository>()

            // List represents GitHub repositories searched from GitHub API
            val searchedRepoList = listOf(testGitHubRepository1, testGitHubRepository2)

            // Search keyword
            val keyword = "repo"

            `when`(localGitHubDatabaseRepository.getMySavedList())
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Success(savedRepoList))
                )
            `when`(gitHubApiRepository.searchGitHubRepositories(keyword))
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Success(GitHubRepositoryList(searchedRepoList)))
                )

            viewModel.updateSearchKeyword(keyword = keyword)
            viewModel.searchGitHubRepositories()

            // Verify that gitHubSearchResultState is updated correctly
            assertTrue(viewModel.gitHubSearchResultState.value is GitHubResponse.Success)
        }

    /**
     * The ViewModel emits a error response containing the corresponding error message
     * retrieved from the `gitHubApiRepository`.
     */
    @Test
    fun `searchGitHubRepositories should emit error response if on error`() =
        runTest {
            val keyword = "repo"
            val errorMessage = "Failed searchGitHubRepositories"

            `when`(localGitHubDatabaseRepository.getMySavedList())
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Success(emptyList()))
                )
            `when`(gitHubApiRepository.searchGitHubRepositories(keyword))
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Error(errorMessage))
                )

            viewModel.updateSearchKeyword(keyword = keyword)
            viewModel.searchGitHubRepositories()

            // Verify that gitHubSearchResultState is updated correctly
            assertTrue(viewModel.gitHubSearchResultState.value is GitHubResponse.Error)
            assertTrue((viewModel.gitHubSearchResultState.value as GitHubResponse.Error).error == errorMessage)
        }

    /**
     * The ViewModel emits a success response containing the corresponding repository data
     * retrieved from the `gitHubApiRepository`.
     */
    @Test
    fun `successful searchGitHubRepositories should indicate whether searched repositories saved in user's saved list`() =
        runTest {
            // List represents GitHub repositories saved in user's saved list
            val savedRepoList = listOf(
                testGitHubRepository1.toLocalGitHubRepository(isSaved = true)
                    .toSavedGitHubRepository(isSaved = true)
            )

            // List represents GitHub repositories searched from GitHub API
            val searchedRepoList = listOf(testGitHubRepository1, testGitHubRepository2)

            // List represents expected GitHub repositories result
            val expectedRepoList = listOf(
                testGitHubRepository1.toLocalGitHubRepository(isSaved = true),
                testGitHubRepository2.toLocalGitHubRepository(isSaved = false)
            )

            // Search keyword
            val keyword = "repo"

            `when`(localGitHubDatabaseRepository.getMySavedList())
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Success(savedRepoList))
                )
            `when`(gitHubApiRepository.searchGitHubRepositories(keyword))
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Success(GitHubRepositoryList(searchedRepoList)))
                )

            viewModel.updateSearchKeyword(keyword = keyword)
            viewModel.searchGitHubRepositories()

            // Verify that gitHubSearchResultState is updated correctly
            assertTrue(viewModel.gitHubSearchResultState.value is GitHubResponse.Success)
            // Verify that GitHub repository is indicated as saved
            assertTrue((viewModel.gitHubSearchResultState.value as GitHubResponse.Success).data == expectedRepoList)
        }

    /**
     * The ViewModel emits a success response containing the corresponding success message
     * retrieved from the `localGitHubDatabaseRepository`.
     */
    @Test
    fun `saveSelectedGitHubRepositoryInDatabase should emit success response if no error`() =
        runTest {
            val testLocalRepo = testGitHubRepository2.toLocalGitHubRepository(isSaved = false)

            `when`(localGitHubDatabaseRepository.saveSelectedGitHubRepositoryInDatabase(testLocalRepo))
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Success(true))
                )

            viewModel.saveSelectedGitHubRepositoryInDatabase(testLocalRepo)

            // Verify that gitHubSearchResultState is updated correctly
            assertTrue(viewModel.isSelectedGitHubRepositorySavedState.value is GitHubResponse.Success)
            assertTrue((viewModel.isSelectedGitHubRepositorySavedState.value as GitHubResponse.Success).data)
        }

    /**
     * The ViewModel emits a error response containing the corresponding error message
     * retrieved from the `localGitHubDatabaseRepository`.
     */
    @Test
    fun `saveSelectedGitHubRepositoryInDatabase should emit error response if on error`() =
        runTest {
            val testLocalRepo = testGitHubRepository2.toLocalGitHubRepository(isSaved = false)
            val errorMessage = "Failed searchGitHubRepositories"

            `when`(localGitHubDatabaseRepository.saveSelectedGitHubRepositoryInDatabase(testLocalRepo))
                .thenReturn(
                    MutableStateFlow(GitHubResponse.Error(errorMessage))
                )

            viewModel.saveSelectedGitHubRepositoryInDatabase(testLocalRepo)

            // Verify that gitHubSearchResultState is updated correctly
            assertTrue(viewModel.isSelectedGitHubRepositorySavedState.value is GitHubResponse.Error)
            assertTrue((viewModel.isSelectedGitHubRepositorySavedState.value as GitHubResponse.Error).error == errorMessage)
        }
}