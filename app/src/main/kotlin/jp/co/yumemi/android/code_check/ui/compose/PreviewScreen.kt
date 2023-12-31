package jp.co.yumemi.android.code_check.ui.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.Owner
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.view_model.GithubRepoViewModel

/**
 * Composable that displays a preview of a repository.
 *
 * @param githubRepoViewModel The ViewModel providing repository information.
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun PreviewScreen(
    githubRepoViewModel: GithubRepoViewModel,
    modifier: Modifier = Modifier
) {
    val repository: RepositoryItem? by githubRepoViewModel.repositoryItem.observeAsState(null)

    repository?.let {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.dp_10))
        ) {
            item {
                ImageSection(owner = it.owner)
            }
            item {
                InfoSection(repository = it)
            }
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
    owner: Owner?,
) {
    val painter = rememberAsyncImagePainter(model = R.drawable.baseline_broken_image_24)
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(owner?.avatarUrl)
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
    repository: RepositoryItem
) {
    RepositoryTitleNameSection(repository.name)
    Column(
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dp_20))
    ) {
        OwnerLoginSection(repository.owner)
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
fun OwnerLoginSection(owner: Owner?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.by),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = owner?.login ?: stringResource(R.string.null_value),
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
        Divider(
            thickness = dimensionResource(id = R.dimen.dp_1),
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.dp_8),
                    bottom = dimensionResource(id = R.dimen.dp_8)
                )

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