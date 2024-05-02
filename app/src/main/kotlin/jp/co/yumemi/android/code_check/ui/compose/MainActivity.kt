/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.ui.compose.navigation.AppNavHost
import jp.co.yumemi.android.code_check.ui.theme.GithubRepositoryAppTheme

/**
 * An activity serving as the entry point of the application.
 * This activity hosts the Jetpack Compose UI for the Github Repository App.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubRepositoryApp()
        }
    }
}

/**
 * The main Composable function for the Github Repository App.
 * This function sets up the theme and navigation for the entire app.
 */
@Composable
fun GithubRepositoryApp() {
    GithubRepositoryAppTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            content = {
                // Host the navigation flow of the app
                AppNavHost(navController = navController)
            }
        )
    }
}