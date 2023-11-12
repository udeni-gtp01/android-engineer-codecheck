package jp.co.yumemi.android.code_check.ui.view

import android.content.Intent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.Owner
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.ui.compose.DataField
import jp.co.yumemi.android.code_check.ui.compose.GoToUrlSection
import jp.co.yumemi.android.code_check.ui.compose.InfoSection
import jp.co.yumemi.android.code_check.ui.compose.OwnerLoginSection
import jp.co.yumemi.android.code_check.ui.compose.RepositoryTitleNameSection
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test class for the PreviewScreen composable.
 */
@RunWith(AndroidJUnit4::class)
class PreviewScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    /**
     * Setup function called before each test case.
     * It initializes the Espresso Intents for testing intent-related functionality.
     */
    @Before
    fun setup() {
        Intents.init()
    }

    /**
     * Teardown function called after each test case.
     * It releases Espresso Intents, cleaning up after testing intent-related functionality.
     */
    @After
    fun tearDown() {
        Intents.release()
    }

    /**
     * Test to verify that the repository information is displayed correctly in the InfoSection.
     * It checks various UI elements and performs a click on the "Go to Repository" button, validating the intent.
     */
    @Test
    fun testPreviewScreenInfoSection_repositoryInfoDisplayed() {
        val repository = RepositoryItem(
            name = "repo 1",
            forksCount = 10,
            language = "language",
            owner = Owner(login = "name", avatarUrl = "url"),
            stargazersCount = 20,
            watchersCount = 30,
            openIssuesCount = 40,
            htmlUrl = "https://example.com"
        )
        composeTestRule.setContent {
            InfoSection(repository = repository)
        }
        val goToRepoButton = composeTestRule.onNodeWithTag("GoToRepoButton")

        composeTestRule.onNodeWithText("repo 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("10").assertIsDisplayed()
        composeTestRule.onNodeWithText("language").assertIsDisplayed()
        composeTestRule.onNodeWithText("name").assertIsDisplayed()
        composeTestRule.onNodeWithText("20").assertIsDisplayed()
        composeTestRule.onNodeWithText("30").assertIsDisplayed()
        composeTestRule.onNodeWithText("40").assertIsDisplayed()

        // Verify that the intent is intended with the correct action and data
        goToRepoButton.performClick()
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_VIEW))
        Intents.intended(IntentMatchers.hasData("https://example.com"))
    }

    /**
     * Test to verify if an empty repository url displays nothing to redirect.
     */
    @Test
    fun testGoToUrlSection_emptyRepoUrl_notDisplayGoToRepoButton() {
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
            GoToUrlSection(url = repository.htmlUrl)
        }
        val goToRepoButton = composeTestRule.onNodeWithTag("GoToRepoButton")

        // Verifying that element tagged as "GoToRepoButton" does not exist.
        goToRepoButton.assertDoesNotExist()
    }

    /**
     * Test to verify if an empty repository name displays nothing for repository name.
     */
    @Test
    fun testRepositoryTitleNameSection_emptyName_notDisplayRepositoryTitle() {
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
            RepositoryTitleNameSection(name = repository.name)
        }

        // Verifying that element tagged as "PreviewRepositoryTitle" does not exist
        val previewRepositoryTitle = composeTestRule.onNodeWithTag("PreviewRepositoryTitle")
        previewRepositoryTitle.assertDoesNotExist()
    }

    /**
     * Test to verify if an empty owner details displays a default null owner name.
     */
    @Test
    fun testOwnerLoginSection_emptyOwner_displayDefaultOwnerName() {
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
            OwnerLoginSection(owner = repository.owner)
        }

        // Verifying that element tagged as "PreviewOwnerName" displays default null values
        val previewOwnerName = composeTestRule.onNodeWithTag("PreviewOwnerName")
        previewOwnerName.assert(hasText(appContext.getString(R.string.null_value)))
    }

    /**
     * Test to verify if an empty owner login name displays a default null owner name.
     */
    @Test
    fun testOwnerLoginSection_emptyOwnerLoginName_displayDefaultOwnerName() {
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
            OwnerLoginSection(owner = repository.owner)
        }

        // Verifying that element tagged as "PreviewOwnerName" displays default null values
        val previewOwnerName = composeTestRule.onNodeWithTag("PreviewOwnerName")
        previewOwnerName.assert(hasText(appContext.getString(R.string.null_value)))
    }

    /**
     * Test to verify if empty data is passed to DataField displays a default null value.
     */
    @Test
    fun testDataField_nullParam_displayDefaultNullValue() {
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
            DataField(
                title = "",
                value = repository.language,
            )
        }

        // Verifying that element tagged as "PreviewDataField" displays default null values
        val previewDataField = composeTestRule.onNodeWithTag("PreviewDataField")
        previewDataField.assert(hasText(appContext.getString(R.string.null_value)))
    }
}