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
                ImageSection(avatarUrl = it.owner.avatarUrl)
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
 * @param avatarUrl The URL of the repository owner's avatar.
 */
@Composable
fun ImageSection(
    avatarUrl: String,
) {
    val painter = rememberAsyncImagePainter(model = R.drawable.baseline_broken_image_24)
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(avatarUrl)
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
    RepositoryTitleName(repository.name)
    Column(
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.dp_20))
    ) {
        OwnerLogin(repository.owner.login)
    }
    Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.dp_20))
    ) {
        DataField(
            title = stringResource(R.string.written_language),
            value = repository.language
        )
        DataField(
            title = stringResource(R.string.stars_count),
            value = repository.stargazersCount.toString()
        )
        DataField(
            title = stringResource(R.string.watchers_count),
            value = repository.watchersCount.toString()
        )
        DataField(
            title = stringResource(R.string.forks_count),
            value = repository.forksCount.toString()
        )
        DataField(
            title = stringResource(R.string.open_issues_count),
            value = repository.openIssuesCount.toString()
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
 * @param name The name of the repository.
 */
@Composable
fun RepositoryTitleName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold
    )
}

/**
 * Composable that displays the owner's login information.
 *
 * @param owner The owner's login name.
 */
@Composable
fun OwnerLogin(owner: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.by),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = owner,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

/**
 * Composable that displays data fields.
 *
 * @param title The title of the data field.
 * @param value The value of the data field.
 */
@Composable
fun DataField(
    title: String,
    value: String?
) {
    value?.let {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Divider(
                thickness = dimensionResource(id = R.dimen.dp_1),
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.dp_8),
                    bottom = dimensionResource(id = R.dimen.dp_8)
                )
            )
        }
    }
}

/**
 * Composable that displays a button to navigate to Github repository.
 *
 * @param url The URL of Github repository to navigate to.
 */
@Composable
fun GoToUrlSection(url: String) {
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