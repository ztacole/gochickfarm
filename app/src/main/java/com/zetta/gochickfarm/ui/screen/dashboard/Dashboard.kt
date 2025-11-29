package com.zetta.gochickfarm.ui.screen.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun Dashboard(
    onNavigateToAddFeeding: () -> Unit,
    onNavigateToBreeding: () -> Unit,
    onNavigateToTransaction: () -> Unit,
    onNavigateToGoatSearch: (category: String) -> Unit,
    onNavigateToChickenSearch: (category: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: DashboardViewModel = koinViewModel()

    DashboardScreen(
        summaryUiState = viewModel.summaryUiState,
        onNavigateToAddFeeding = onNavigateToAddFeeding,
        onNavigateToBreeding = onNavigateToBreeding,
        onNavigateToTransaction = onNavigateToTransaction,
        onNavigateToGoatSearch = onNavigateToGoatSearch,
        onNavigateToChickenSearch = onNavigateToChickenSearch,
        modifier = modifier
    )
}