package jp.co.yumemi.android.code_check.ui.compose

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import jp.co.yumemi.android.code_check.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubRepoAppTopAppBar(){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
            )
        }
    )
}