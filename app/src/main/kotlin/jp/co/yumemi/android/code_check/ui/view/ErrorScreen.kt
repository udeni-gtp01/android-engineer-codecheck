package jp.co.yumemi.android.code_check.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.yumemi.android.code_check.R

@Composable
fun ErrorScreen(
    errorMessage: String,
    onRetryButtonClicked: () -> Any,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier

            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadErrorSection(
            errorMessage = errorMessage,
            onRetryButtonClicked = onRetryButtonClicked,
        ) // Circular progress indicator
    }
}

@Composable
fun LoadErrorSection(
    errorMessage: String,
    onRetryButtonClicked: () -> Any?,
    modifier: Modifier = Modifier

) {
//    Column(horizontalAlignment= Alignment.CenterHorizontally,
//        modifier=Modifier.fillMaxWidth()
//        ) {
    Text(
//           text = "${stringResource(id = R.string.something_went_wrong)}",
        text = errorMessage,
//        color = CommonColorScheme.gray,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(16.dp),
        overflow = TextOverflow.Visible
    )
//   }
    Spacer(modifier = Modifier.height(16.dp))
//    Column(
//        modifier = Modifier.padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//
//    ) {
    Button(
//        colors = ButtonDefaults.outlinedButtonColors(contentColor = CommonColorScheme.dark_text),
        onClick = { onRetryButtonClicked() },
        modifier = Modifier
            .fillMaxWidth()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        CommonColorScheme.shade_yellow,
//                        CommonColorScheme.main_orange,
//                    )
//                ),
//                shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
//            )
    ) {
        Text(text = stringResource(R.string.retry))
    }
//        OutlinedButton(
//            onClick = {
//                FirebaseCrashlytics.getInstance().log(errorMessage)
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = stringResource(R.string.report_problem))
//        }
//    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewErrorScreen() {
    ErrorScreen(
        errorMessage = "err",
        onRetryButtonClicked = {},

        )
}

