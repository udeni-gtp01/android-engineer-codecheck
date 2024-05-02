package jp.co.yumemi.android.code_check.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import jp.co.yumemi.android.code_check.ui.theme.Typography

/**
 * Composable function for displaying a custom top app bar for the GitHub repository app.
 * This top app bar contains the title and description of the screen.
 *
 * @param title Resource ID for the title string displayed in the top app bar.
 * @param description Resource ID for the description string displayed in the top app bar.
 * @param modifier Modifier for adjusting the layout of this composable.
 * @param isFilled Boolean indicating whether the top app bar should have a filled background.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubRepoAppTopAppBar(
    @StringRes title: Int,
    @StringRes description: Int,
    modifier: Modifier = Modifier,
    isFilled: Boolean = true
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    stringResource(id = title),
                    style = Typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    stringResource(id = description),
                    style = Typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        modifier = modifier,
        // Customizing top app bar colors based on whether it should have a filled background
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isFilled) {
                MaterialTheme.colorScheme.surface
            } else {
                Color.Transparent
            }
        ),
    )
}