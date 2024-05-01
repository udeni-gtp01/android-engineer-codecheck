package jp.co.yumemi.android.code_check.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.GitHubRepository
import jp.co.yumemi.android.code_check.model.GitHubRepositoryOwner

@Composable
fun MySavedListScreen(
    modifier: Modifier = Modifier,

) {

}

@Composable
fun ShowMySavedList() {

}

/**
 * Composable function for displaying the repository list search result.
 *
 * @param repositoryList List of repository items.
 * @param onGitHubRepositoryClicked Callback function when a single repository list item is clicked.
 */
@Composable
fun MySavedListSection(
    repositoryList: List<GitHubRepository>,
    onGitHubRepositoryClicked: (GitHubRepository) -> Unit,
) {
    if (repositoryList.isEmpty()) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dp_10))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.no_result))
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dp_12)),
            contentPadding = PaddingValues(
                top = dimensionResource(id = R.dimen.dp_12),
                bottom = dimensionResource(id = R.dimen.dp_12)
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
 * Composable function for displaying a single repository item.
 *
 * @param githubRepository The repository item to display.
 * @param onGitHubRepositoryClicked Callback function when this item is clicked.
 */
@Composable
fun MySavedListItem(
    githubRepository: GitHubRepository,
    onGitHubRepositoryClicked: (GitHubRepository) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onGitHubRepositoryClicked(githubRepository) }
            .fillMaxWidth()
            .border(
                width = dimensionResource(R.dimen.dp_1),
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(dimensionResource(R.dimen.dp_5))
            )
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(dimensionResource(R.dimen.dp_5))
            )
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dp_10))
        ) {
            MySavedRepositoryNameSection(githubRepository.name)
            MySavedRepositoryOwnerSection(githubRepository.owner)
            HorizontalDivider(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.dp_8),
                    bottom = dimensionResource(id = R.dimen.dp_8)
                ),
                thickness = dimensionResource(id = R.dimen.dp_1)
            )
            MySavedRepositoryLanguageAndStatisticsSection(
                language = githubRepository.language,
                watchersCount = githubRepository.watchersCount,
                stargazersCount = githubRepository.stargazersCount
            )
        }
    }
}

/**
 * Composable function for displaying the repository name.
 *
 * @param name The name of the repository. If null, nothing is displayed.
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
 * Composable function for displaying the owner section.
 *
 * @param owner The owner of the repository. If null, a default null value is displayed.
 */
@Composable
fun MySavedRepositoryOwnerSection(owner: GitHubRepositoryOwner?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.by),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = owner?.login ?: stringResource(R.string.null_value),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag("ResultOwnerName")
        )
    }
}

/**
 * Composable function for displaying language and statistics section.
 *
 * @param language The programming language used in the repository. If null, a default null value is displayed.
 * @param watchersCount The number of watchers for the repository. If null, a default null value is displayed.
 * @param stargazersCount The number of stargazers for the repository. If null, a default null value is displayed.
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
        // Section for the language used in the repository
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val languagePainter: Painter = painterResource(id = R.drawable.outline_code)
            Icon(
                painter = languagePainter,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
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
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(id = R.dimen.sp_18))
            )
            Text(
                text = String.format(
                    stringResource(R.string.watchers_summary),
                    watchersCount ?: stringResource(R.string.null_value),
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
                tint = MaterialTheme.colorScheme.primary,
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