package jp.co.yumemi.android.code_check.ui.view

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.Owner
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.repository.GithubRepository
import jp.co.yumemi.android.code_check.view_model.GithubRepoViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Test class for the HomeScreen composable.
 */
@RunWith(MockitoJUnitRunner::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var githubRepository: GithubRepository

    private lateinit var viewModel: GithubRepoViewModel

    // CompositionLocal to provide the search keyword in the Composable
    private val LocalSearchKeyword =
        compositionLocalOf<Any> { error("No search keyword provided") }

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = GithubRepoViewModel(githubRepository)
    }


    /**
     * Test to verify entering a valid keyword in the search text field.
     */
    @Test
    fun enterValidKeywordInSearchTextField() {
        val searchKeyword = "repo"
        composeTestRule.setContent {
            CompositionLocalProvider(LocalSearchKeyword provides searchKeyword) {
                SearchSection(
                    searchKeyword = searchKeyword,
                    onSearchKeywordChange = {},
                    onSearchClicked = {},
                    onClearButtonClicked = {}
                )
            }
        }

        val searchField = composeTestRule.onNodeWithTag("SearchTextField")

        searchField.assertIsDisplayed()
        searchField.assert(hasText(searchKeyword))
    }

    /**
     * Test to verify that the Clear Button clears the search field and ViewModel searchKeyword.
     */
    @Test
    fun testClearButton_emptySearchField() {
        val searchKeyword = "repo"

        composeTestRule.setContent {
            HomeScreen(
                onRepositoryItemClicked = {},
                githubRepoViewModel = viewModel
            )
        }

        val searchField = composeTestRule.onNodeWithTag("SearchTextField")
        val clearButton = composeTestRule.onNodeWithTag("ClearButton")

        searchField.performTextInput(searchKeyword)
        // Check if ClearButton is displayed
        clearButton.assertIsDisplayed()

        // Check if SearchTextField has the initial text
        searchField.assert(hasText(searchKeyword))

        // Perform a click on the ClearButton
        clearButton.performClick()

        // Verify that the searchKeyword in the ViewModel is cleared
        Assert.assertEquals("", viewModel.searchKeyword)

        // Wait for the UI to update
        composeTestRule.waitForIdle()

        // Verify that SearchTextField has no text
        searchField.assert(hasText(""))
    }

    /**
     * Test to verify empty search field results in an empty result message.
     */
    @Test
    fun emptySearchField_emptyResultDisplayed() {
        composeTestRule.setContent {
            HomeScreen(
                onRepositoryItemClicked = {},
                githubRepoViewModel = viewModel
            )
        }

        val searchField = composeTestRule.onNodeWithTag("SearchTextField")
        val searchButton = composeTestRule.onNodeWithTag("SearchButton")

        // Verify that the search field is initially empty
        searchField.assert(hasText(""))

        searchButton.performClick()
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText(appContext.getString(R.string.no_result))
            .assertIsDisplayed()
    }

    /**
     * Test to verify the SearchResultSection displays the correct repository items.
     */
    @Test
    fun testSearchResultSection() {
        val repository1 = RepositoryItem(
            id = "1",
            name = "repo 1",
            forksCount = 10,
            language = "language",
            owner = Owner(login = "name", avatarUrl = "url"),
            stargazersCount = 10,
            watchersCount = 10,
            openIssuesCount = 10,
            htmlUrl = "htmlurl"
        )
        val repository2 = RepositoryItem(
            id = "2",
            name = "repo 2",
            forksCount = 10,
            language = "",
            owner = Owner(login = "name", avatarUrl = "url"),
            stargazersCount = 10,
            watchersCount = 10,
            openIssuesCount = 10,
            htmlUrl = "htmlurl"
        )
        val repoListResult = listOf(repository1, repository2)
        composeTestRule.setContent {
            CompositionLocalProvider(LocalSearchKeyword provides repoListResult) {
                SearchResultSection(
                    repositoryList = repoListResult,
                    onRepositoryItemClicked = {}
                )
            }
        }
        composeTestRule
            .onNode(
                hasText("repo 1"),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        composeTestRule
            .onNode(
                hasText("repo 2"),
                useUnmergedTree = true
            )
            .assertIsDisplayed()

    }
}