package jp.co.yumemi.android.code_check.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import jp.co.yumemi.android.code_check.model.GitHubRepository
import jp.co.yumemi.android.code_check.model.GitHubRepositoryList
import jp.co.yumemi.android.code_check.model.GitHubRepositoryOwner
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.repository.GitHubApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GithubRepoViewModelTest {
    //ensure that LiveData updates happen on the same thread in JUnit tests
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @ObsoleteCoroutinesApi
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: HomeSharedViewModel

    @Mock
    private lateinit var gitHubApiRepository: GitHubApiRepository

    @Mock
    lateinit var gitHubRepositoryObserver: Observer<GitHubRepository>

    @Mock
    lateinit var gitHubApiResultObserver: Observer<GitHubResponse<GitHubRepositoryList>>

    @OptIn(ObsoleteCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = HomeSharedViewModel(gitHubApiRepository)
        viewModel.gitHubRepository.observeForever(gitHubRepositoryObserver)
        viewModel.gitHubApiResult.observeForever(gitHubApiResultObserver)
    }

    @After
    fun tearDown() {
        viewModel.gitHubRepository.removeObserver(gitHubRepositoryObserver)
        viewModel.gitHubApiResult.removeObserver(gitHubApiResultObserver)
    }

    @Test
    fun `initially repository list should be empty`() {
        assertTrue(viewModel.getRepositoryList().isEmpty())
    }

    @Test
    fun `initially search keyword should be empty`() {
        assertTrue(viewModel.searchKeyword.isEmpty())
    }

    @Test
    fun `provided valid keyword should update the repository list`() =
        runTest {// Uses Main’s scheduler
            val repository1 = GitHubRepository(
                name = "result repo 1/repo 1",
                forksCount = 10,
                language = "",
                owner = GitHubRepositoryOwner(login = "name", avatarUrl = "url"),
                stargazersCount = 10,
                watchersCount = 10,
                openIssuesCount = 10,
                htmlUrl = "html url"
            )
            val repository2 = GitHubRepository(
                name = "result repo 2/repo 2",
                forksCount = 20,
                language = "",
                owner = GitHubRepositoryOwner(login = "name", avatarUrl = "url"),
                stargazersCount = 20,
                watchersCount = 20,
                openIssuesCount = 20,
                htmlUrl = "html url"
            )
            val repoListResult = listOf(repository1, repository2)

            val keyword = "repo"

            // Mock the behavior of githubRepository.searchRepositoryList
            Mockito.`when`(gitHubApiRepository.searchGitHubRepositories(keyword))
                .thenReturn(
                    MutableStateFlow(
                        GitHubResponse.Success(
                            GitHubRepositoryList(
                                repoListResult
                            )
                        )
                    )
                )

            viewModel.updateSearchKeyword(keyword = keyword)
            viewModel.searchGitHubRepositories()
            assertEquals(repoListResult, viewModel.getRepositoryList())
        }

    @Test
    fun `provided invalid keyword should empty the repository list`() = runTest {
        val expectedRepoListResult = emptyList<GitHubRepository>()
        viewModel.updateSearchKeyword(keyword = "    ")
        viewModel.searchGitHubRepositories()
        assertEquals(expectedRepoListResult, viewModel.getRepositoryList())
    }

    @Test
    fun `provided empty keyword should empty the repository list`() = runTest {
        val expectedRepoListResult = emptyList<GitHubRepository>()
        viewModel.updateSearchKeyword(keyword = "")
        viewModel.searchGitHubRepositories()
        assertEquals(expectedRepoListResult, viewModel.getRepositoryList())
    }

    @Test
    fun `test clearSearchKeyword should clear the keyword`() = runTest {
        viewModel.clearSearchKeyword()
        assertTrue(viewModel.searchKeyword.isEmpty())
    }

    @Test
    fun `test setRepository should update the repository list`() {
        val repositoryItem = GitHubRepository(
            name = "result repo 1/repo 1",
            forksCount = 10,
            language = "language",
            owner = GitHubRepositoryOwner(login = "name", avatarUrl = "url"),
            stargazersCount = 10,
            watchersCount = 10,
            openIssuesCount = 10,
            htmlUrl = "html url"
        )
        viewModel.setRepository(repositoryItem)
        assertEquals(repositoryItem, viewModel.gitHubRepository.value)
    }
}