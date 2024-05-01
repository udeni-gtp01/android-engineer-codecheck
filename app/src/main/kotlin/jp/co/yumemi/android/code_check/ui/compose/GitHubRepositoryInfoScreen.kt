package jp.co.yumemi.android.code_check.ui.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.viewModel.GitHubRepositoryInfoViewModel

/**
 * Composable that displays a preview of a repository.
 *
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun GitHubRepositoryInfoScreen(
    modifier: Modifier = Modifier,
    gitHubRepositoryInfoViewModel: GitHubRepositoryInfoViewModel = hiltViewModel()
) {
    val gitHubRepositoryInfoState =
        gitHubRepositoryInfoViewModel.gitHubRepositoryInfo.collectAsState(initial = null)

    gitHubRepositoryInfoState.let {
        it.value?.let { repositoryInfoState ->
            GitHubRepositoryMainInfoSection(
                repositoryInfoState = repositoryInfoState,
                onSaveButtonClick = gitHubRepositoryInfoViewModel::addGitHubRepositoryToMySavedList,
                onUnSaveButtonClick = gitHubRepositoryInfoViewModel::removeGitHubRepositoryFromMySavedList,
                modifier = modifier
            )
        }
    }
}

@Composable
fun GitHubRepositoryMainInfoSection(
    repositoryInfoState: GitHubResponse<LocalGitHubRepository?>,
    onSaveButtonClick: (localGitHubRepository: LocalGitHubRepository, onSuccess: () -> Unit, onError: (String?) -> Unit, onLoading: () -> Unit) -> Unit,
    onUnSaveButtonClick: (localGitHubRepository: LocalGitHubRepository, onSuccess: () -> Unit, onError: (String?) -> Unit, onLoading: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (repositoryInfoState is GitHubResponse.Success) {
        repositoryInfoState.data?.let {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.dp_10))
            ) {
                item {
                    SaveToMySavedListSection(
                        repository = it,
                        onSaveButtonClick = onSaveButtonClick,
                        onUnSaveButtonClick = onUnSaveButtonClick
                    )
                }
                item {
                    ImageSection(ownerAvatarUrl = it.ownerAvatarUrl)
                }
                item {
                    InfoSection(repository = it)
                }
            }
        }
    }
}

@Composable
fun SaveToMySavedListSection(
    repository: LocalGitHubRepository,
    onSaveButtonClick: (localGitHubRepository: LocalGitHubRepository, onSuccess: () -> Unit, onError: (String?) -> Unit, onLoading: () -> Unit) -> Unit,
    onUnSaveButtonClick: (localGitHubRepository: LocalGitHubRepository, onSuccess: () -> Unit, onError: (String?) -> Unit, onLoading: () -> Unit) -> Unit,
) {
    Surface(

    ) {
        var isSaved by remember { mutableStateOf(false) }
        isSaved = repository.isSaved

        // Track save operation loading state
        var isSaveResponseLoading by remember { mutableStateOf(false) }
        ElevatedButton(
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
                imageVector = if (isSaved) Icons.Default.FavoriteBorder else Icons.Filled.Favorite,
                contentDescription = if (isSaved) "Save me" else "Unsave me",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = if (isSaved) "Unsave me" else "Save me")
        }
    }
}

/**
 * Composable that displays the image of the repository owner's avatar.
 *
 * @param owner The owner of the repository. If null, a placeholder image is displayed.
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
 * Composable that displays the information section of the repository preview.
 *
 * @param repository The repository item to display information for.
 */
@Composable
fun InfoSection(
    repository: LocalGitHubRepository
) {
    RepositoryTitleNameSection(repository.name)
    Column(
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dp_20))
    ) {
        OwnerLoginSection(repository.ownerLogin)
    }
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.dp_20))
    ) {
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
    Column(
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.dp_20))
    ) {
        GoToUrlSection(repository.htmlUrl)
    }
}

/**
 * Composable that displays the name of the repository.
 *
 * @param name The name of the repository. If null, nothing is displayed.
 */
@Composable
fun RepositoryTitleNameSection(name: String?) {
    name?.let {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("PreviewRepositoryTitle")
        )
    }
}

/**
 * Composable that displays the login name of the repository owner.
 *
 * @param owner The owner of the repository. If null, a default null value is displayed.
 */
@Composable
fun OwnerLoginSection(ownerLoginName: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
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
 * Composable that displays a field of data.
 *
 * @param title The title of the data field.
 * @param value The value to display. If null, a default null value is displayed.
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
 * Composable that displays a button to navigate to Github repository.
 *
 * @param url The URL of Github repository to navigate to. If null, the button is not displayed.
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