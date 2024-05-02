package jp.co.yumemi.android.code_check.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.SavedGitHubRepository
import jp.co.yumemi.android.code_check.ui.theme.GithubRepositoryAppTheme
import jp.co.yumemi.android.code_check.viewModel.MySavedListViewModel

/**
 * Composable function for displaying the screen of user's saved GitHub repositories.
 *
 * @param modifier Modifier for styling and layout customization.
 * @param mySavedListViewModel ViewModel for managing the state and logic of the saved repository screen.
 * @param navigateToGitHubRepositoryInfo Callback function for navigating to detailed repository information.
 */
@Composable
fun MySavedListScreen(
    modifier: Modifier = Modifier,
    mySavedListViewModel: MySavedListViewModel = hiltViewModel(),
    navigateToGitHubRepositoryInfo: () -> Unit,
) {
    // Collecting the state of the user's saved GitHub repository list
    val mySavedListState = mySavedListViewModel.mySavedListState.collectAsState()

    // Collecting the state of the selected GitHub repository
    val isSelectedGitHubRepositorySavedState =
        mySavedListViewModel.isSelectedGitHubRepositorySavedState.collectAsState()

    // State variable to hold the navigation condition
    var shouldNavigateToGitHubRepositoryInfo by remember { mutableStateOf(false) }

    // Fetching the user's saved GitHub repository list when the composable is launched
    LaunchedEffect(mySavedListState) {
        mySavedListViewModel.getMySavedList()
    }
    Scaffold(
        topBar = {
            GithubRepoAppTopAppBar(
                title = R.string.saved_screen_title,
                description = R.string.saved_screen_description,
                modifier = modifier,
                isFilled = false
            )
        },
    ) { padding ->
        Column(
            modifier = modifier.padding(padding)
        ) {
            // Handling different states of the user's saved GitHub repository list
            when (mySavedListState.value) {
                is GitHubResponse.Loading -> {
                    LoadingScreen()
                }

                is GitHubResponse.Error -> {
                    ErrorScreen(
                        errorTitle = R.string.oh_no,
                        errorCode = (mySavedListState.value as GitHubResponse.Error).error,
                        onRetryButtonClicked = { mySavedListViewModel.getMySavedList() }
                    )
                }

                is GitHubResponse.Success -> {
                    val mySavedListResult = mySavedListState.value as GitHubResponse.Success
                    MySavedListSection(
                        repositoryList = mySavedListResult.data,
                        onGitHubRepositoryClicked = { selectedGitHubRepository ->
                            mySavedListViewModel.saveSelectedGitHubRepositoryInDatabase(
                                selectedGitHubRepository
                            )
                            shouldNavigateToGitHubRepositoryInfo = true
                        }
                    )
                }
            }
        }
    }

    // Check if navigation is required and perform navigation
    if (shouldNavigateToGitHubRepositoryInfo) {
        if (isSelectedGitHubRepositorySavedState.value is GitHubResponse.Success) {
            navigateToGitHubRepositoryInfo()
            shouldNavigateToGitHubRepositoryInfo = false // Reset flag after navigation
        } else if (isSelectedGitHubRepositorySavedState.value is GitHubResponse.Error) {
            ErrorScreen(
                errorTitle = R.string.oh_no,
                errorCode = (mySavedListState.value as GitHubResponse.Error).error,
                onRetryButtonClicked = { mySavedListViewModel.getMySavedList() }
            )
        }
    }
}

/**
 * Composable function for displaying the section of the user's saved GitHub repository list.
 *
 * @param repositoryList List of saved GitHub repository items.
 * @param onGitHubRepositoryClicked Callback function when a saved repository item is clicked.
 */
@Composable
fun MySavedListSection(
    repositoryList: List<SavedGitHubRepository>,
    onGitHubRepositoryClicked: (SavedGitHubRepository) -> Unit,
) {
    if (repositoryList.isEmpty()) {
        // Displaying a message if the saved repository list is empty
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dp_10))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.empty_saved_list))
        }
    } else {
        // Displaying the saved GitHub repository list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dp_12)),
            contentPadding = PaddingValues(
                top = dimensionResource(id = R.dimen.dp_12),
                bottom = dimensionResource(id = R.dimen.dp_12),
                start = dimensionResource(id = R.dimen.dp_10),
                end = dimensionResource(id = R.dimen.dp_10)
            )
        ) {
            items(items = repositoryList) {
                MySavedListItem(
                    githubRepository = it,
                    onGitHubRepositoryClicked = onGitHubRepositoryClicked
                )
            }
        }
    }
}

/**
 * Composable function for displaying a single item of the user's saved GitHub repository list.
 *
 * @param githubRepository The saved repository item to display.
 * @param onGitHubRepositoryClicked Callback function when this item is clicked.
 */
@Composable
fun MySavedListItem(
    githubRepository: SavedGitHubRepository,
    onGitHubRepositoryClicked: (SavedGitHubRepository) -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.clickable { onGitHubRepositoryClicked(githubRepository) }
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.dp_10))
        ) {
            // Section for displaying the GitHub repository name
            MySavedRepositoryNameSection(githubRepository.name)
            // Section for displaying the owner of the repository
            MySavedRepositoryOwnerSection(githubRepository.ownerLogin)
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.dp_10)))
            // Section for displaying language and statistics of the repository
            MySavedRepositoryLanguageAndStatisticsSection(
                language = githubRepository.language,
                watchersCount = githubRepository.watchersCount,
                stargazersCount = githubRepository.stargazersCount
            )
        }
    }
}

/**
 * Composable function for displaying the section of the GitHub repository name.
 *
 * @param name The name of the saved GitHub repository. If null, nothing is displayed.
 */
@Composable
fun MySavedRepositoryNameSection(name: String?) {
    name?.let {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("ResultRepoName")
        )
    }
}

/**
 * Composable function for displaying the section of the GitHub repository owner.
 *
 * @param ownerLoginName The login name of the owner of the saved GitHub repository.
 */
@Composable
fun MySavedRepositoryOwnerSection(ownerLoginName: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.by),
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = ownerLoginName ?: stringResource(R.string.null_value),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag("ResultOwnerName")
        )
    }
}

/**
 * Composable function for displaying the section of language and statistics of the GitHub repository.
 *
 * @param language The programming language used in the saved GitHub repository.
 * @param watchersCount The number of watchers for the saved GitHub repository.
 * @param stargazersCount The number of stargazers for the saved GitHub repository.
 */
@Composable
fun MySavedRepositoryLanguageAndStatisticsSection(
    language: String?,
    watchersCount: Long?,
    stargazersCount: Long?
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Section for displaying the programming language
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val languagePainter: Painter = painterResource(id = R.drawable.outline_code)
            Icon(
                painter = languagePainter,
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(id = R.dimen.sp_18))
            )
            Text(
                text = String.format(
                    stringResource(R.string.language_summary),
                    language ?: stringResource(R.string.null_value)
                ),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag("ResultLanguage")
            )
        }

        // Section for the number of watchers
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val watcherPainter: Painter = painterResource(id = R.drawable.outline_watcher)
            Icon(
                painter = watcherPainter,
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(id = R.dimen.sp_18))
            )
            Text(
                text = String.format(
                    stringResource(R.string.watchers_summary),
                    watchersCount ?: stringResource(R.string.null_value)
                ),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag("ResultWatchers")
            )
        }

        // Section for the number of stargazers
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val starPainter: Painter = painterResource(id = R.drawable.outline_star_rate)
            Icon(
                painter = starPainter,
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(id = R.dimen.sp_18))
            )
            Text(
                text = String.format(
                    stringResource(R.string.star_summary),
                    stargazersCount ?: stringResource(R.string.null_value),
                ),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag("ResultStargazers")
            )
        }
    }
}

/**
 * Preview function for the MySavedListItem composable, used for Compose UI preview.
 * This is useful during the development process.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMySavedListItem() {
    val testGitHubRepository = SavedGitHubRepository(
        id = 123,
        forksCount = 50,
        language = "Kotlin",
        name = "My Awesome Project",
        openIssuesCount = 2,
        stargazersCount = 100,
        watchersCount = 75,
        htmlUrl = "https://github.com/user/my-awesome-project",
        ownerLogin = "user",
        ownerAvatarUrl = "https://avatars.githubusercontent.com/user",
        isSaved = true
    )
    GithubRepositoryAppTheme {
        MySavedListItem(githubRepository = testGitHubRepository) {
        }
    }
}