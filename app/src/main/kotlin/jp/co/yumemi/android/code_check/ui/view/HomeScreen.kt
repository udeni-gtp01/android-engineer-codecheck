package jp.co.yumemi.android.code_check.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.view_model.GithubRepoViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    onRepositoryItemClicked: () -> Unit,
    sharedViewModel: GithubRepoViewModel,
    modifier: Modifier = Modifier
) {
    val repositoryList: List<RepositoryItem>? by sharedViewModel.repositoryList.observeAsState(
        initial = null
    )
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.dp_10))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.dp_10),
                    bottom = dimensionResource(id = R.dimen.dp_10)
                )
        ) {
            SearchSection(
                searchKeyword = sharedViewModel.searchKeyword,
                onSearchKeywordChange = { sharedViewModel.updateSearchKeyword(it) },
                onSearchClicked = {
                    keyboardController?.hide()
                    sharedViewModel.getRepositoryList()
                },
                onClearButtonClicked = { sharedViewModel.clearSearchKeyword() },
            )
        }
        repositoryList?.let {
            SearchResultSection(
                repositoryList = it,
                onRepositoryItemClicked = { selectedRepository ->
                    sharedViewModel.setSelectedRepositoryItem(selectedRepository)
                    onRepositoryItemClicked()
                },
            )
        }
    }
}

@Composable
fun SearchSection(
    searchKeyword: String,
    onSearchKeywordChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onClearButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchKeyword,
        singleLine = true,
        onValueChange = onSearchKeywordChange,
        leadingIcon = {
            SearchButton(onSearchClicked = onSearchClicked)
        },
        trailingIcon = {
            if (searchKeyword.isNotBlank()) {
                ClearButton(onClearButtonClicked = onClearButtonClicked)
            }
        },
        label = {
            Text(text = stringResource(R.string.searchInputText_hint))
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClicked()
            }
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun SearchButton(onSearchClicked: () -> Unit) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search_icon_description)
        )
    }
}

@Composable
fun ClearButton(onClearButtonClicked: () -> Unit) {
    IconButton(onClick = { onClearButtonClicked() }) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = stringResource(R.string.clear_icon_description)
        )
    }
}

@Composable
fun SearchResultSection(
    repositoryList: List<RepositoryItem>,
    onRepositoryItemClicked: (RepositoryItem) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dp_12)),
        contentPadding = PaddingValues(
            top = dimensionResource(id = R.dimen.dp_12),
            bottom = dimensionResource(id = R.dimen.dp_12)
        )
    ) {
        items(
            items = repositoryList,
            key = { repositoryItem ->
                // Return a stable, unique key for the repository item
                repositoryItem.id
            }) {
            RepositoryListItem(
                githubRepository = it,
                onRepositoryItemClicked = onRepositoryItemClicked
            )
        }
    }
}

@Composable
fun RepositoryListItem(
    githubRepository: RepositoryItem,
    onRepositoryItemClicked: (RepositoryItem) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onRepositoryItemClicked(githubRepository) }
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
            RepositoryName(githubRepository.name)
            OwnerSection(githubRepository.owner.login)
            Divider(
                thickness = dimensionResource(id = R.dimen.dp_1),
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.dp_8),
                    bottom = dimensionResource(id = R.dimen.dp_8)
                )
            )
            LanguageAndStatisticsSection(
                language = githubRepository.language,
                watchersCount = githubRepository.watchersCount,
                stargazersCount = githubRepository.stargazersCount
            )
        }
    }
}

@Composable
fun RepositoryName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun OwnerSection(owner: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.by),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = owner,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun LanguageAndStatisticsSection(
    language: String?,
    watchersCount: Long,
    stargazersCount: Long
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        language?.let {
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
                        it,
                    ),
                    style = MaterialTheme.typography.bodySmall,

                    )
            }
        }
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
                    watchersCount,
                ),
                style = MaterialTheme.typography.bodySmall,
            )
        }
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
                    stargazersCount,
                ),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}