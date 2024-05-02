package jp.co.yumemi.android.code_check.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.model.GitHubResponse
import jp.co.yumemi.android.code_check.model.LocalGitHubRepository
import jp.co.yumemi.android.code_check.viewModel.HomeViewModel

/**
 * Composable function for the home screen of the application.
 *
 * @param modifier Modifier for styling and layout customization.
 * @param homeViewModel ViewModel for managing the state and logic of the home screen.
 * @param navigateToGitHubRepositoryInfo Callback function for navigating to detailed GitHub repository information.
 * @param onMySavedListButtonClick Callback function for the click event of the "Saved" FAB.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToGitHubRepositoryInfo: () -> Unit,
    onMySavedListButtonClick: () -> Unit
) {
    // Collecting the state of the GitHub search result
    val gitHubSearchResultState = homeViewModel.gitHubSearchResultState.collectAsState()

    // Collecting the state of the selected GitHub repository
    val isSelectedGitHubRepositorySavedState =
        homeViewModel.isSelectedGitHubRepositorySavedState.collectAsState()

    // Accessing the keyboard controller
    val keyboardController = LocalSoftwareKeyboardController.current

    // State variable to hold the navigation condition
    var shouldNavigateToGitHubRepositoryInfo by remember { mutableStateOf(false) }

    // Fetching recent updates from the database when the composable is launched
    LaunchedEffect(gitHubSearchResultState) {
        homeViewModel.getRecentUpdateFromDatabase()
    }
    Scaffold(
        topBar = {
            GithubRepoAppTopAppBar(
                title = R.string.app_name,
                description = R.string.home_screen_description,
                modifier = modifier
            )
        },
        floatingActionButton = { MySavedListButton(onMySavedListButtonClick) }
    ) { padding ->
        Column(
            modifier = modifier.padding(padding)
        ) {
            // Section for the search field
            SearchSection(
                searchKeyword = homeViewModel.searchKeyword,
                onSearchKeywordChange = { homeViewModel.updateSearchKeyword(it) },
                onSearchClicked = {
                    keyboardController?.hide()
                    homeViewModel.searchGitHubRepositories()
                },
                onClearButtonClicked = { homeViewModel.clearSearchKeyword() },
            )

            // Handling different states of the GitHub search result
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
                    // Section for displaying the search result
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

/**
 * Composable function for the "Saved" FAB.
 *
 * @param onMySavedListButtonClick Callback function for the click event of the button.
 */
@Composable
fun MySavedListButton(onMySavedListButtonClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onMySavedListButtonClick,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_collections_bookmark_24),
                contentDescription = stringResource(id = R.string.saved),
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
        },
        text = { Text(text = stringResource(id = R.string.saved)) },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    )
}

/**
 * Composable function for the search field section of the home screen.
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
        // Text field for entering the search keyword
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
 * Composable function for displaying the GitHub repository list search result.
 *
 * @param repositoryList List of GitHub repository items.
 * @param onGitHubRepositoryClicked Callback function when a single repository list item is clicked.
 */
@Composable
fun SearchResultSection(
    repositoryList: List<LocalGitHubRepository>,
    onGitHubRepositoryClicked: (LocalGitHubRepository) -> Unit,
) {
    if (repositoryList.isEmpty()) {
        // Displaying a message when no results are found
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
        // Lazy column for displaying the list of searched GitHub repositories
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.dp_12)),
            contentPadding = PaddingValues(
                top = dimensionResource(id = R.dimen.dp_12),
                bottom = dimensionResource(id = R.dimen.dp_12),
                start = dimensionResource(id = R.dimen.dp_10),
                end = dimensionResource(id = R.dimen.dp_10)
            )
        ) {
            items(items = repositoryList) {
                // Displaying each repository item as a list item
                RepositoryListItem(
                    localGitHubRepository = it,
                    onGitHubRepositoryClicked = onGitHubRepositoryClicked
                )
            }
        }
    }
}

/**
 * Composable function for displaying a single GitHub repository item.
 *
 * @param localGitHubRepository The repository item to display.
 * @param onGitHubRepositoryClicked Callback function when this item is clicked.
 */
@Composable
fun RepositoryListItem(
    localGitHubRepository: LocalGitHubRepository,
    onGitHubRepositoryClicked: (LocalGitHubRepository) -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier.clickable { onGitHubRepositoryClicked(localGitHubRepository) }
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.dp_10))
        ) {
            // Displaying bookmark icon if the GitHub repository is saved
            ShowSavedIcon(localGitHubRepository.isSaved)
            // Section for repository name
            RepositoryNameSection(localGitHubRepository.name)
            // Section for owner
            OwnerSection(localGitHubRepository.ownerLogin)
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.dp_10)))
            // Section for language and statistic
            LanguageAndStatisticsSection(
                language = localGitHubRepository.language,
                watchersCount = localGitHubRepository.watchersCount,
                stargazersCount = localGitHubRepository.stargazersCount
            )
        }
    }
}

/**
 * Composable function for displaying the bookmark icon for a GitHub repository.
 *
 * @param isSaved Boolean indicating whether the repository is saved.
 */
@Composable
fun ShowSavedIcon(isSaved: Boolean) {
    // Displaying the bookmark icon if the repository is saved
    if (isSaved) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_bookmark_24),
                contentDescription = stringResource(id = R.string.saved),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
        }
    }
}

/**
 * Composable function for displaying the GitHub repository name.
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
 * @param ownerLoginName The owner's login name of the repository. If null, a default null value is displayed.
 */
@Composable
fun OwnerSection(ownerLoginName: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.by),
            style = MaterialTheme.typography.bodySmall,
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
        // Section for displaying the programming language
        Row(verticalAlignment = Alignment.CenterVertically) {
            val languagePainter: Painter = painterResource(id = R.drawable.outline_code)
            Icon(
                painter = languagePainter,
                contentDescription = null,
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