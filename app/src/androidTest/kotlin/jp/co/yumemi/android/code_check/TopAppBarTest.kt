package jp.co.yumemi.android.code_check

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import jp.co.yumemi.android.code_check.ui.theme.GithubRepositoryAppTheme
import jp.co.yumemi.android.code_check.ui.view.GithubRepoAppTopAppBar
import org.junit.Rule
import org.junit.Test

class TopAppBarTest{
    //This rule lets us set the Compose content under test and interact with it
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun githubRepoAppTopAppBarTest(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.setContent {
            GithubRepositoryAppTheme {
                GithubRepoAppTopAppBar()
            }
        }
        composeTestRule.onNodeWithText(appContext.getString(R.string.app_name))
    }
}