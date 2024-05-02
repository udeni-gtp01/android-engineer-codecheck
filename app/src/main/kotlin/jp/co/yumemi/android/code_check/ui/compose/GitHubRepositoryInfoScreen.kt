package jp.co.yumemi.android.code_check.ui.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.viewModel.GitHubRepositoryInfoViewModel

/**
 * Composable function for displaying the GitHub repository information screen.
 * This screen shows detailed information about a GitHub repository.
 *
 * @param modifier Modifier for adjusting the layout of this composable.
 * @param gitHubRepositoryInfoViewModel ViewModel for managing the state and logic of information screen.
 */
@Composable
fun GitHubRepositoryInfoScreen(
    modifier: Modifier = Modifier,
    gitHubRepositoryInfoViewModel: GitHubRepositoryInfoViewModel = hiltViewModel()
) {
    // Collecting GitHub repository information state
    val gitHubRepositoryInfoState =
        gitHubRepositoryInfoViewModel.gitHubRepositoryInfo.collectAsState(initial = null)

    // Checking if the fetched data is successfully received
    if (gitHubRepositoryInfoState.value is GitHubResponse.Success) {
        val gitHubRepositoryInfoResult = gitHubRepositoryInfoState.value as GitHubResponse.Success
        gitHubRepositoryInfoResult.data?.let { repository ->
            Scaffold(
                topBar = {
                    GithubRepoAppTopAppBar(
                        title = R.string.info_screen_title,
                        description = R.string.info_screen_description,
                        modifier = modifier,
                        isFilled = false
                    )
                },
                bottomBar = {
                    // Bottom app bar for displaying save/unsave button
                    var isSaved by remember { mutableStateOf(false) }
                    isSaved = repository.isSaved

                    // Track save operation loading state
                    var isSaveResponseLoading by remember { mutableStateOf(false) }

                    // Callback function to handle the save button click
                    val onSaveButtonClick =
                        gitHubRepositoryInfoViewModel::addGitHubRepositoryToMySavedList

                    // Callback function to handle the remove from saved button click
                    val onUnSaveButtonClick =
                        gitHubRepositoryInfoViewModel::removeGitHubRepositoryFromMySavedList
                    BottomAppBar(
                        actions = {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Button for saving/unsaving repository
                                TextButton(
                                    onClick = {
                                        // Only allow saving operation when not loading
                                        if (!isSaveResponseLoading) {
                                            isSaved = !isSaved
                                            if (isSaved) {
                                                // Call to add GitHub repository to user's saved list
                                                onSaveButtonClick(
                                                    repository,
                                                    {
                                                        // Reset loading state on success
                                                        isSaveResponseLoading = false
                                                    },
                                                    {
                                                        // Reset loading state on error
                                                        isSaveResponseLoading = false

                                                        // Revert the saved status on error
                                                        isSaved = !isSaved
                                                    },
                                                    {
                                                        // Set loading state on loading
                                                        isSaveResponseLoading = true
                                                    }
                                                )
                                            } else {
                                                // Call to remove GitHub repository from user's saved list
                                                onUnSaveButtonClick(
                                                    repository,
                                                    {
                                                        // Reset loading state on success
                                                        isSaveResponseLoading = false
                                                    },
                                                    {
                                                        // Reset loading state on error
                                                        isSaveResponseLoading = false

                                                        // Revert the saved status on error
                                                        isSaved = !isSaved
                                                    },
                                                    {
                                                        // Set loading state on loading
                                                        isSaveResponseLoading = true
                                                    }
                                                )
                                            }
                                        }
                                    }) {
                                    Icon(
                                        painter = if (isSaved) painterResource(id = R.drawable.baseline_bookmark_remove_24) else painterResource(
                                            id = R.drawable.baseline_bookmark_add_24
                                        ),
                                        contentDescription = if (isSaved) stringResource(id = R.string.unsave) else stringResource(
                                            id = R.string.save
                                        ),
                                        modifier = Modifier.size(ButtonDefaults.IconSize)
                                    )
                                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                    Text(
                                        text = if (isSaved) stringResource(id = R.string.unsave) else stringResource(
                                            id = R.string.save
                                        )
                                    )
                                }
                            }
                        },
                    )
                },
            ) { padding ->
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(padding)
                ) {
                    // Displaying repository image section
                    item {
                        ImageSection(ownerAvatarUrl = repository.ownerAvatarUrl)
                    }
                    // Displaying repository information section
                    item {
                        InfoSection(repository = repository)
                    }
                }
            }
        }
    }
}

/**
 * Composable function for displaying the image of the repository owner's avatar.
 *
 * @param ownerAvatarUrl The URL of the repository owner's avatar image.
 */
@Composable
fun ImageSection(
    ownerAvatarUrl: String?,
) {
    val painter = rememberAsyncImagePainter(model = R.drawable.baseline_broken_image_24)
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(ownerAvatarUrl)
            .crossfade(true)
            .build(),
        placeholder = painter,
        error = painter,
        fallback = painter,
        contentDescription = stringResource(R.string.image_description),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.dp_200))
            .padding(dimensionResource(id = R.dimen.dp_20))
    )
}

/**
 * Composable function for displaying the information section of the repository.
 *
 * @param repository The repository item containing information to display.
 */
@Composable
fun InfoSection(
    repository: LocalGitHubRepository
) {
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.dp_20))
    ) {
        // Displaying repository name
        RepositoryTitleNameSection(repository.name)
        // Displaying owner login name
        OwnerLoginSection(repository.ownerLogin)
        // Displaying data fields (language, stars count, watchers count, fork count, open issue count)
        DataField(
            title = stringResource(R.string.written_language),
            value = repository.language,
        )
        DataField(
            title = stringResource(R.string.stars_count),
            value = repository.stargazersCount.toString(),
        )
        DataField(
            title = stringResource(R.string.watchers_count),
            value = repository.watchersCount.toString(),
        )
        DataField(
            title = stringResource(R.string.forks_count),
            value = repository.forksCount.toString(),
        )
        DataField(
            title = stringResource(R.string.open_issues_count),
            value = repository.openIssuesCount.toString(),
        )
    }
    // Displaying button to navigate to repository URL
    Row(
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.dp_20))
    ) {
        GoToUrlSection(repository.htmlUrl)
    }
}

/**
 * Composable function for displaying the name of the repository.
 *
 * @param name The name of the repository.
 */
@Composable
fun RepositoryTitleNameSection(name: String?) {
    name?.let {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .testTag("PreviewRepositoryTitle")
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Composable function for displaying the login name of the repository owner.
 *
 * @param ownerLoginName The login name of the repository owner.
 */
@Composable
fun OwnerLoginSection(ownerLoginName: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen.dp_20))
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.by),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = ownerLoginName ?: stringResource(R.string.null_value),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag("PreviewOwnerName")
        )
    }
}

/**
 * Composable function for displaying a field of data.
 *
 * @param title The title of the data field.
 * @param value The value to display.
 */
@Composable
fun DataField(
    title: String,
    value: String?,
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = value ?: stringResource(R.string.null_value),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("PreviewDataField")
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.dp_8),
                    bottom = dimensionResource(id = R.dimen.dp_8)
                ),
            thickness = dimensionResource(id = R.dimen.dp_1)
        )
    }
}

/**
 * Composable function for displaying a button to navigate to the GitHub repository URL.
 *
 * @param url The URL of the GitHub repository.
 */
@Composable
fun GoToUrlSection(url: String?) {
    url?.let {
        val context = LocalContext.current
        Button(
            onClick = {
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
                context.startActivity(urlIntent)
            },
            modifier = Modifier.testTag("GoToRepoButton")
        ) {
            Text(
                text = stringResource(id = R.string.go_to_repository),
            )
        }
    }
}