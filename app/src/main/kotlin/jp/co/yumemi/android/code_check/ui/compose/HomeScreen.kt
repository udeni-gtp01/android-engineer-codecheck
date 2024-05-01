package jp.co.yumemi.android.code_check.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.viewModel.HomeViewModel

/**
 * Composable function representing the main screen of the app.
 *
 * @param navigateToGitHubRepositoryInfo Callback function when a repository item is clicked.
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToGitHubRepositoryInfo: () -> Unit,
    onMySavedListButtonClick: () -> Unit
) {
    val gitHubSearchResultState = homeViewModel.gitHubSearchResultState.collectAsState()
    val isSelectedGitHubRepositorySavedState =
        homeViewModel.isSelectedGitHubRepositorySavedState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // State variable to hold the navigation condition
    var shouldNavigateToGitHubRepositoryInfo by remember { mutableStateOf(false) }
    LaunchedEffect(gitHubSearchResultState) {
        homeViewModel.getRecentUpdateFromDatabase()
    }
    Scaffold(
        topBar = {
            GithubRepoAppTopAppBar()
        },
        floatingActionButton = {
            MySavedListButton(onMySavedListButtonClick)
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
        ) {
            SearchSection(
                searchKeyword = homeViewModel.searchKeyword,
                onSearchKeywordChange = { homeViewModel.updateSearchKeyword(it) },
                onSearchClicked = {
                    keyboardController?.hide()
                    homeViewModel.searchGitHubRepositories()
                },
                onClearButtonClicked = { homeViewModel.clearSearchKeyword() },
            )

            when (gitHubSearchResultState.value) {
                is GitHubResponse.Loading -> {
                    LoadingScreen()
                }

                is GitHubResponse.Error -> {
                    ErrorScreen(
                        errorTitle = R.string.oh_no,
                        errorCode = (gitHubSearchResultState.value as GitHubResponse.Error).error,
                        onRetryButtonClicked = { homeViewModel.searchGitHubRepositories() }
                    )
                }

                is GitHubResponse.Success -> {
                    val gitHubSearchResult = gitHubSearchResultState.value as GitHubResponse.Success
                    SearchResultSection(
                        repositoryList = gitHubSearchResult.data,
                        onGitHubRepositoryClicked = { selectedGitHubRepository ->
                            homeViewModel.saveSelectedGitHubRepositoryInDatabase(
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
                errorCode = (gitHubSearchResultState.value as GitHubResponse.Error).error,
                onRetryButtonClicked = { homeViewModel.searchGitHubRepositories() }
            )
        }
    }
}

@Composable
fun MySavedListButton(onMySavedListButtonClick: () -> Unit) {
    FloatingActionButton(
        shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
        onClick = onMySavedListButtonClick,
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp, 56.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiary
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

/**
 * Composable function for the search field section of the main screen.
 *
 * @param searchKeyword The search keyword.
 * @param onSearchKeywordChange Callback function for changes in the search keyword.
 * @param onSearchClicked Callback function for the search button click.
 * @param onClearButtonClicked Callback function for the clear button click.
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun SearchSection(
    searchKeyword: String,
    onSearchKeywordChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onClearButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(dimensionResource(id = R.dimen.dp_10))
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
                .then(Modifier.testTag("SearchTextField"))
        )
    }
}

/**
 * Composable function for the search button.
 *
 * @param onSearchClicked Callback function for the search button click.
 */
@Composable
fun SearchButton(onSearchClicked: () -> Unit) {
    IconButton(
        onClick = { onSearchClicked() },
        modifier = Modifier.testTag("SearchButton")
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search_icon_description)
        )
    }
}

/**
 * Composable function for the clear button.
 *
 * @param onClearButtonClicked Callback function for the clear button click.
 */
@Composable
fun ClearButton(onClearButtonClicked: () -> Unit) {
    IconButton(
        onClick = { onClearButtonClicked() },
        modifier = Modifier.testTag("ClearButton")
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = stringResource(R.string.clear_icon_description)
        )
    }
}

/**
 * Composable function for displaying the repository list search result.
 *
 * @param repositoryList List of repository items.
 * @param onGitHubRepositoryClicked Callback function when a single repository list item is clicked.
 */
@Composable
fun SearchResultSection(
    repositoryList: List<LocalGitHubRepository>,
    onGitHubRepositoryClicked: (LocalGitHubRepository) -> Unit,
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
                RepositoryListItem(
                    localGitHubRepository = it,
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
fun RepositoryListItem(
    localGitHubRepository: LocalGitHubRepository,
    onGitHubRepositoryClicked: (LocalGitHubRepository) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onGitHubRepositoryClicked(localGitHubRepository) }
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
            ShowSavedIcon(localGitHubRepository.isSaved)
            RepositoryNameSection(localGitHubRepository.name)
            OwnerSection(localGitHubRepository.ownerLogin)
            HorizontalDivider(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.dp_8),
                    bottom = dimensionResource(id = R.dimen.dp_8)
                ),
                thickness = dimensionResource(id = R.dimen.dp_1)
            )
            LanguageAndStatisticsSection(
                language = localGitHubRepository.language,
                watchersCount = localGitHubRepository.watchersCount,
                stargazersCount = localGitHubRepository.stargazersCount
            )
        }
    }
}

@Composable
fun ShowSavedIcon(isSaved: Boolean) {
    if (isSaved) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
    }
}

/**
 * Composable function for displaying the repository name.
 *
 * @param name The name of the repository. If null, nothing is displayed.
 */
@Composable
fun RepositoryNameSection(name: String?) {
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
fun OwnerSection(ownerLoginName: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.by),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = ownerLoginName ?: stringResource(R.string.null_value),
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
fun LanguageAndStatisticsSection(
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