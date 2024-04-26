/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.ui.compose.navigation.AppNavHost
import jp.co.yumemi.android.code_check.ui.theme.GithubRepositoryAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubRepositoryApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubRepositoryApp() {
    GithubRepositoryAppTheme {
        val navController = rememberNavController()
        Scaffold(
            topBar = {
                GithubRepoAppTopAppBar()
            },
            content = {
                // Host the navigation flow of the app
                AppNavHost(
                    navController = navController, modifier = Modifier.padding(it)
                )
            }
        )
    }
}
