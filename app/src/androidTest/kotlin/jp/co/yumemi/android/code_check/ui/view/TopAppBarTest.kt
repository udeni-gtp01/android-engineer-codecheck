package jp.co.yumemi.android.code_check

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import jp.co.yumemi.android.code_check.ui.theme.GithubRepositoryAppTheme
import jp.co.yumemi.android.code_check.ui.view.GithubRepoAppTopAppBar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testGithubRepoAppTopAppBar() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.setContent {
            GithubRepositoryAppTheme {
                GithubRepoAppTopAppBar()
            }
        }
        composeTestRule
            .onNodeWithText(appContext.getString(R.string.app_name))
            .assertIsDisplayed()
    }
}