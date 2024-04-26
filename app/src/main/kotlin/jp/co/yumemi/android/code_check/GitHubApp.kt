package jp.co.yumemi.android.code_check

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom application class for the GitHub App.
 *
 * This class is responsible for initializing the application and configuring Hilt for dependency injection.
 */
@HiltAndroidApp
class GitHubApp : Application()