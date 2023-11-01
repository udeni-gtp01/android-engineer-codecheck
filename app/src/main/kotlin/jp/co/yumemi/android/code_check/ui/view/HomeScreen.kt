package jp.co.yumemi.android.code_check.ui.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.Owner
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.ui.theme.GithubRepositoryAppTheme
import jp.co.yumemi.android.code_check.view_model.SharedViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    onRepositoryItemClicked: () -> Unit,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Log.d("oyasumi", "SharedViewModel hash code: ${System.identityHashCode(sharedViewModel)}")

    val repositoryList: List<RepositoryItem>? by sharedViewModel.repositoryList.observeAsState(initial = null)
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.dp_10))

    ) {
        SearchSection(
            searchKeyword = sharedViewModel.searchKeyword,
            onSearchKeywordChange = { sharedViewModel.updateSearchKeyword(it) },
            onSearchClicked = {
                keyboardController?.hide()
                sharedViewModel.getRepositoryList()
            },
            onClearSearchClicked = { sharedViewModel.clearSearchKeyword() },
        )
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
    onClearSearchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchKeyword,
        singleLine = true,
        onValueChange = onSearchKeywordChange,
        leadingIcon = {
            IconButton(onClick = {
                onSearchClicked()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_icon_description)
                )
            }
        },
        trailingIcon = {
            if (searchKeyword.isNotBlank()) {
                IconButton(onClick = { onClearSearchClicked() }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_icon_description)
                    )
                }
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
fun SearchResultSection(
    repositoryList: List<RepositoryItem>,
    onRepositoryItemClicked: (RepositoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
//    val listState = rememberLazyListState()
    LazyColumn(
//        state = listState,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dp_12)),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
//        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.dp_10))
    ) {
        items(
            items = repositoryList,
            key = { repositoryItem ->
                // Return a stable, unique key for the repository item
                repositoryItem.id
            }) {
            ResultItem(githubRepository = it, onRepositoryItemClicked = onRepositoryItemClicked)
        }
    }
}

@Composable
fun ResultItem(githubRepository: RepositoryItem, onRepositoryItemClicked: (RepositoryItem) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onRepositoryItemClicked(githubRepository) }
    ) {
        Text(text = githubRepository.name)
    }
    Divider(thickness = dimensionResource(id = R.dimen.dp_1))
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewSearchSection() {
    GithubRepositoryAppTheme {
        Column {
            SearchSection(
                searchKeyword = "test",
                onSearchKeywordChange = { },
                onSearchClicked = { },
                onClearSearchClicked = {}
            )
            var repository1 = RepositoryItem(
                id = "1",
                name = "result repo 1/repo 1",
                forksCount = 10,
                language = "",
                owner = Owner(avatarUrl = "url"),
                stargazersCount = 10,
                watchersCount = 10,
                openIssuesCount = 10
            )
            var repository2 = RepositoryItem(
                id = "2",
                name = "result repo 2/repo 2",
                forksCount = 10,
                language = "",
                owner = Owner(avatarUrl = "url"),
                stargazersCount = 10,
                watchersCount = 10,
                openIssuesCount = 10
            )
            var result = listOf(repository1, repository2)

            SearchResultSection(result, {})
        }
    }
}