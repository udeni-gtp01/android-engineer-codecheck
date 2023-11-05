package jp.co.yumemi.android.code_check.ui.view

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.view_model.GithubRepoViewModel

@Composable
fun PreviewScreen(
    githubRepoViewModel: GithubRepoViewModel,
    modifier: Modifier = Modifier
) {
    val repository: RepositoryItem? by githubRepoViewModel.repositoryItem.observeAsState(null)
    Log.d("oyasumi", "preview-GithubRepoViewModel hash code: ${System.identityHashCode(githubRepoViewModel)}")

    repository?.let {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.dp_10))
        ) {
            item {
                ImageSection(avatarUrl = it.owner.avatarUrl)
            }
            item {
                DataSection(repository = it)
            }
        }
    }
}

@Composable
fun ImageSection(
    avatarUrl: String,
    modifier: Modifier = Modifier
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

@Composable
fun DataSection(
    repository: RepositoryItem
) {
    Text(text = repository.name)
    Divider(thickness = dimensionResource(id = R.dimen.dp_2))
    Text(text = String.format(stringResource(R.string.written_language), repository.language))
    Text(text = String.format(stringResource(R.string.stars_count), repository.stargazersCount))
    Text(text = String.format(stringResource(R.string.watchers_count), repository.watchersCount))
    Text(text = String.format(stringResource(R.string.forks_count), repository.forksCount))
    Text(
        text = String.format(
            stringResource(R.string.open_issues_count),
            repository.openIssuesCount
        )
    )
}