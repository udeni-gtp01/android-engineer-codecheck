package jp.co.yumemi.android.code_check.ui.view

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.Owner
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.repository.GithubRepository
import jp.co.yumemi.android.code_check.ui.compose.HomeScreen
import jp.co.yumemi.android.code_check.ui.compose.LanguageAndStatisticsSection
import jp.co.yumemi.android.code_check.ui.compose.RepositoryListItem
import jp.co.yumemi.android.code_check.ui.compose.SearchResultSection
import jp.co.yumemi.android.code_check.ui.compose.SearchSection
import jp.co.yumemi.android.code_check.view_model.GithubRepoViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Test class for the HomeScreen composable.
 */
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var githubRepository: GithubRepository

    private lateinit var viewModel: GithubRepoViewModel

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
    fun enterValidKeywordInSearchTextField_searchFieldDisplayKeyword() {
        val searchKeyword = "repo"
        composeTestRule.setContent {
            SearchSection(
                searchKeyword = searchKeyword,
                onSearchKeywordChange = {},
                onSearchClicked = {},
                onClearButtonClicked = {}
            )
        }

        val searchField = composeTestRule.onNodeWithTag("SearchTextField")

        searchField.assertIsDisplayed()
        searchField.assert(hasText(searchKeyword))
    }

    /**
     * Test to verify that the ViewModel searchKeyword holds the keyword entered in the search
     * text field when Search button is clicked.
     */
    @Test
    fun testSearchButton_viewModelHoldsKeyword() {
        val searchKeyword = "repo"

        composeTestRule.setContent {
            HomeScreen(
                onRepositoryItemClicked = {},
                githubRepoViewModel = viewModel
            )
        }

        val searchField = composeTestRule.onNodeWithTag("SearchTextField")
        val searchButton = composeTestRule.onNodeWithTag("SearchButton")

        searchField.performTextInput(searchKeyword)

        // Check if SearchTextField has the initial text
        searchField.assert(hasText(searchKeyword))

        // Perform a click on the Search Button
        searchButton.performClick()

        // Verify that the searchKeyword in the ViewModel is cleared
        Assert.assertEquals(searchKeyword, viewModel.searchKeyword)
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
     * Test to verify whether empty keyword search displays a no result message.
     */
    @Test
    fun emptySearchField_displayEmptyResult() {
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

        // Verify that No results text is displayed
        composeTestRule
            .onNodeWithText(appContext.getString(R.string.no_result))
            .assertIsDisplayed()
    }

    /**
     * Test to verify the SearchResultSection displays the correct repository items.
     */
    @Test
    fun testSearchResultSection_displayListOfRepo() {
        val repository1 = RepositoryItem(
            name = "repo 1",
            forksCount = 10,
            language = "language",
            owner = Owner(login = "name", avatarUrl = "url"),
            stargazersCount = 10,
            watchersCount = 10,
            openIssuesCount = 10,
            htmlUrl = "html url"
        )
        val repository2 = RepositoryItem(
            name = "repo 2",
            forksCount = 10,
            language = "",
            owner = Owner(login = "name", avatarUrl = "url"),
            stargazersCount = 10,
            watchersCount = 10,
            openIssuesCount = 10,
            htmlUrl = "html url"
        )
        val repoListResult = listOf(repository1, repository2)
        composeTestRule.setContent {
            SearchResultSection(
                repositoryList = repoListResult,
                onRepositoryItemClicked = {}
            )
        }

        // Verifying if both repositories' names are displayed
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

    /**
     * Test to verify if an empty repository name in RepositoryListItem displays nothing for repository name.
     */
    @Test
    fun testRepositoryNameSection_emptyRepoName_notDisplayRepoName() {
        val repository = RepositoryItem(
            name = null,
            forksCount = null,
            language = null,
            owner = null,
            stargazersCount = null,
            watchersCount = null,
            openIssuesCount = null,
            htmlUrl = null
        )
        composeTestRule.setContent {
            RepositoryListItem(
                githubRepository = repository,
                onRepositoryItemClicked = {}
            )
        }

        // Verifying that element tagged as "ResultRepoName" does not exist.
        val resultRepoName = composeTestRule.onNodeWithTag("ResultRepoName")
        resultRepoName.assertDoesNotExist()
    }

    /**
     * Test to verify if an empty owner details in RepositoryListItem displays a default null owner name.
     */
    @Test
    fun testOwnerSection_emptyOwner_displayDefaultOwnerName() {
        val repository = RepositoryItem(
            name = null,
            forksCount = null,
            language = null,
            owner = null,
            stargazersCount = null,
            watchersCount = null,
            openIssuesCount = null,
            htmlUrl = null
        )
        composeTestRule.setContent {
            RepositoryListItem(
                githubRepository = repository,
                onRepositoryItemClicked = {}
            )
        }

        // Verifying that element tagged as "ResultOwnerName" displays default null values
        val resultOwnerName =
            composeTestRule.onNodeWithTag("ResultOwnerName", useUnmergedTree = true)
        resultOwnerName.assert(hasText(appContext.getString(R.string.null_value)))
    }

    /**
     * Test to verify if an empty owner login name in RepositoryListItem displays a default null owner name.
     */
    @Test
    fun testOwnerSection_emptyOwnerLoginName_displayDefaultOwnerName() {
        val repository = RepositoryItem(
            name = null,
            forksCount = null,
            language = null,
            owner = Owner(login = null, avatarUrl = null),
            stargazersCount = null,
            watchersCount = null,
            openIssuesCount = null,
            htmlUrl = null
        )
        composeTestRule.setContent {
            RepositoryListItem(
                githubRepository = repository,
                onRepositoryItemClicked = {}
            )
        }

        // Verifying that element tagged as "ResultOwnerName" displays default null values
        val resultOwnerName =
            composeTestRule.onNodeWithTag("ResultOwnerName", useUnmergedTree = true)
        resultOwnerName.assert(hasText(appContext.getString(R.string.null_value)))
    }

    /**
     * Test to verify if LanguageAndStatisticsSection displays default null values when provided
     * with null parameters.
     */
    @Test
    fun testLanguageAndStatisticsSection_nullParams_displayDefaultNullValues() {
        val repository = RepositoryItem(
            name = null,
            forksCount = null,
            language = null,
            owner = Owner(login = null, avatarUrl = null),
            stargazersCount = null,
            watchersCount = null,
            openIssuesCount = null,
            htmlUrl = null
        )
        composeTestRule.setContent {
            LanguageAndStatisticsSection(
                language = repository.language,
                stargazersCount = repository.stargazersCount,
                watchersCount = repository.watchersCount
            )
        }

        // Verifying that elements tagged as "ResultLanguage", "ResultWatchers", and
        // "ResultStargazers"display default null values
        val resultLanguage = composeTestRule.onNodeWithTag("ResultLanguage")
        val resultWatchers = composeTestRule.onNodeWithTag("ResultWatchers")
        val resultStargazers = composeTestRule.onNodeWithTag("ResultStargazers")

        resultLanguage.assert(
            hasText(
                String.format(
                    appContext.getString(R.string.language_summary),
                    appContext.getString(R.string.null_value)
                )
            )
        )
        resultWatchers.assert(
            hasText(
                String.format(
                    appContext.getString(R.string.watchers_summary),
                    appContext.getString(R.string.null_value)
                )
            )
        )
        resultStargazers.assert(
            hasText(
                String.format(
                    appContext.getString(R.string.star_summary),
                    appContext.getString(R.string.null_value)
                )
            )
        )
    }
}