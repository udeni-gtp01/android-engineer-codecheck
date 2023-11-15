package jp.co.yumemi.android.code_check.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import jp.co.yumemi.android.code_check.MainDispatcherRule
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.Owner
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.model.ServerResult
import jp.co.yumemi.android.code_check.repository.GithubRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
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
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: GithubRepoViewModel

    @Mock
    private lateinit var githubRepository: GithubRepository

    @Mock
    lateinit var repositoryItemObserver: Observer<RepositoryItem>

    @Mock
    lateinit var serverResultObserver: Observer<ServerResult<GitHubResponse>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = GithubRepoViewModel(githubRepository)
        viewModel.repositoryItem.observeForever(repositoryItemObserver)
        viewModel.serverResult.observeForever(serverResultObserver)
    }

    @After
    fun tearDown() {
        viewModel.repositoryItem.removeObserver(repositoryItemObserver)
        viewModel.serverResult.removeObserver(serverResultObserver)
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
            val repository1 = RepositoryItem(
                name = "result repo 1/repo 1",
                forksCount = 10,
                language = "",
                owner = Owner(login = "name", avatarUrl = "url"),
                stargazersCount = 10,
                watchersCount = 10,
                openIssuesCount = 10,
                htmlUrl = "html url"
            )
            val repository2 = RepositoryItem(
                name = "result repo 2/repo 2",
                forksCount = 20,
                language = "",
                owner = Owner(login = "name", avatarUrl = "url"),
                stargazersCount = 20,
                watchersCount = 20,
                openIssuesCount = 20,
                htmlUrl = "html url"
            )
            val repoListResult = listOf(repository1, repository2)

            val keyword = "repo"

            // Mock the behavior of githubRepository.searchRepositoryList
            Mockito.`when`(githubRepository.searchRepositoryList(keyword))
                .thenReturn(ServerResult.Success(GitHubResponse(repoListResult)))

            viewModel.updateSearchKeyword(keyword = keyword)
            viewModel.searchRepositoryList()
            assertEquals(repoListResult, viewModel.getRepositoryList())
        }

    @Test
    fun `provided invalid keyword should empty the repository list`() = runTest {
        val expectedRepoListResult = emptyList<RepositoryItem>()
        viewModel.updateSearchKeyword(keyword = "    ")
        viewModel.searchRepositoryList()
        assertEquals(expectedRepoListResult, viewModel.getRepositoryList())
    }

    @Test
    fun `provided empty keyword should empty the repository list`() = runTest {
        val expectedRepoListResult = emptyList<RepositoryItem>()
        viewModel.updateSearchKeyword(keyword = "")
        viewModel.searchRepositoryList()
        assertEquals(expectedRepoListResult, viewModel.getRepositoryList())
    }

    @Test
    fun `test clearSearchKeyword should clear the keyword`() = runTest {
        viewModel.clearSearchKeyword()
        assertTrue(viewModel.searchKeyword.isEmpty())
    }

    @Test
    fun `test setRepository should update the repository list`() {
        val repositoryItem = RepositoryItem(
            name = "result repo 1/repo 1",
            forksCount = 10,
            language = "language",
            owner = Owner(login = "name", avatarUrl = "url"),
            stargazersCount = 10,
            watchersCount = 10,
            openIssuesCount = 10,
            htmlUrl = "html url"
        )
        viewModel.setRepository(repositoryItem)
        assertEquals(repositoryItem, viewModel.repositoryItem.value)
    }
}