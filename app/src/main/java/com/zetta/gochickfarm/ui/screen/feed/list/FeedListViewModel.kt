package com.zetta.gochickfarm.ui.screen.feed.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.gochickfarm.data.model.Feed
import com.zetta.gochickfarm.data.repository.FeedRepository
import com.zetta.gochickfarm.utils.Constants
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class FeedListViewModel(private val repository: FeedRepository): ViewModel() {
    var uiState by mutableStateOf(FeedListUiState())
        private set

    private var currentPage = 1
    private val searchQueryFlow = MutableStateFlow(uiState.search)

    init {
        fetchFeeds()

        viewModelScope.launch {
            searchQueryFlow
                .drop(1)
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    refreshFeeds()
                }
        }
    }

    private fun fetchFeeds() {
        if (uiState.isLoading || uiState.isLoadingMore || uiState.endReached) return
        uiState = uiState.copy(isLoadingMore = true)

        viewModelScope.launch {
            if (currentPage == 1) {
                uiState = uiState.copy(isLoading = true)
            }

            repository.getFeeds(
                currentPage,
                Constants.PAGINATION_LIMIT,
                uiState.search
            ).onSuccess {
                val updatedFeeds = if (currentPage == 1) it.data else uiState.feeds + it.data
                currentPage = it.meta.currentPage

                uiState = uiState.copy(
                    feeds = updatedFeeds,
                    isLoading = false,
                    isLoadingMore = false,
                    isRefreshing = false,
                    endReached = it.meta.currentPage == it.meta.totalPages
                )
            }.onFailure {
                uiState = uiState.copy(
                    errorMessage = it.message,
                    isLoading = false,
                    isLoadingMore = false,
                    isRefreshing = false
                )
            }

            if (!uiState.endReached) currentPage++
        }
    }

    fun refreshFeeds() {
        currentPage = 1
        uiState = uiState.copy(
            feeds = emptyList(),
            isRefreshing = true,
            endReached = false
        )
        fetchFeeds()
    }

    fun loadMoreFeeds() = fetchFeeds()

    fun updateSearchQuery(query: String) {
        uiState = uiState.copy(search = query.ifEmpty { null })
        searchQueryFlow.value = uiState.search
    }

    data class FeedListUiState(
        val feeds: List<Feed> = emptyList(),
        val isLoading: Boolean = false,
        val search: String? = null,
        val isLoadingMore: Boolean = false,
        val isRefreshing: Boolean = false,
        val endReached: Boolean = false,
        val errorMessage: String? = null
    )
}