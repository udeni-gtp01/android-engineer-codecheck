package jp.co.yumemi.android.code_check.ui.view

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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.Owner
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.ui.theme.GithubRepositoryAppTheme
import jp.co.yumemi.android.code_check.view_model.HomeViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val repositoryList: List<RepositoryItem>? by homeViewModel.repositoryList.observeAsState(initial = null)
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        SearchSection(
            searchKeyword = homeViewModel.searchKeyword,
            onSearchKeywordChange = { homeViewModel.updateSearchKeyword(it) },
            onSearchClicked = {
                keyboardController?.hide()
                homeViewModel.getRepositoryList()
            },
            onClearSearchClicked = { homeViewModel.clearSearchKeyword() },
            modifier = modifier
        )
        repositoryList?.let { SearchResultSection(it) }
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
                    contentDescription = stringResource(R.string.searchIconDescription)
                )
            }
        },
        trailingIcon = {
            if (searchKeyword.isNotBlank()) {
                IconButton(onClick = { onClearSearchClicked() }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clearIconDescription)
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
            .padding(horizontal = dimensionResource(id = R.dimen.dp_10))
    )
}

@Composable
fun SearchResultSection(
    repositoryList: List<RepositoryItem>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dp_12)),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.dp_10))

    ) {
        items(repositoryList) {
            ResultItem(it)
        }
    }
}

@Composable
fun ResultItem(githubRepository: RepositoryItem) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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
                name = "result repo/repo",
                forksCount = 10,
                language = "",
                owner = Owner(avatarUrl = "url"),
                stargazersCount = 10,
                watchersCount = 10,
                openIssuesCount = 10
            )
            var repository2 = RepositoryItem(
                name = "result repo/repo",
                forksCount = 10,
                language = "",
                owner = Owner(avatarUrl = "url"),
                stargazersCount = 10,
                watchersCount = 10,
                openIssuesCount = 10
            )
            var result = listOf(repository1, repository2)

            SearchResultSection(result)
        }
    }
}