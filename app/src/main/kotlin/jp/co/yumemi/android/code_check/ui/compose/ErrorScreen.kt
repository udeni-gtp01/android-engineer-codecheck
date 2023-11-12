package jp.co.yumemi.android.code_check.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import jp.co.yumemi.android.code_check.R

/**
 * Composable function for displaying an error screen with a title, error message, and retry button.
 *
 * @param errorTitle The title of the error message.
 * @param errorMessage The detailed error message.
 * @param onRetryButtonClicked Callback to handle the retry button click.
 */
@Composable
fun ErrorScreen(
    errorTitle: String,
    errorMessage: String,
    onRetryButtonClicked: () -> Any,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadErrorSection(
            errorTitle = errorTitle,
            errorMessage = errorMessage,
            onRetryButtonClicked = onRetryButtonClicked,
        )
    }
}

/**
 * Composable function for rendering the content of the error screen, including the title,
 * error message, and a retry button.
 *
 * @param errorTitle The title of the error message.
 * @param errorMessage The detailed error message.
 * @param onRetryButtonClicked Callback to handle the retry button click.
 */
@Composable
fun LoadErrorSection(
    errorTitle: String,
    errorMessage: String,
    onRetryButtonClicked: () -> Any?,
) {
    Text(
        text = errorTitle,
        style = MaterialTheme.typography.titleLarge,
        overflow = TextOverflow.Visible,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp_12)))

    Text(
        text = errorMessage,
        style = MaterialTheme.typography.bodyMedium,
        overflow = TextOverflow.Visible,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp_18)))

    OutlinedButton(
        onClick = { onRetryButtonClicked() },
    ) {
        Text(text = stringResource(R.string.retry))
    }
}

/**
 * Preview function for the ErrorScreen composable, used for Compose UI preview.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewErrorScreen() {
    ErrorScreen(
        errorTitle = "Oh no!",
        errorMessage = "error description",
        onRetryButtonClicked = {},
    )
}