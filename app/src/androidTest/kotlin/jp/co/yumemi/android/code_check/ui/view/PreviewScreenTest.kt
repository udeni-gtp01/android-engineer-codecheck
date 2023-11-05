package jp.co.yumemi.android.code_check.ui.view

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.yumemi.android.code_check.model.Owner
import jp.co.yumemi.android.code_check.model.RepositoryItem
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
     * Test case to verify that the repository information is displayed correctly in the InfoSection.
     * It checks various UI elements and performs a click on the "Go to Repository" button, validating the intent.
     */
    @Test
    fun testPreviewScreenInfoSection_repositoryInfoDisplayed() {
        val repository = RepositoryItem(
            id = "1",
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
}