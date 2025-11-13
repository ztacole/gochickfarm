package com.zetta.gochickfarm.ui.screen.dashboard

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zetta.gochickfarm.data.model.Summary
import com.zetta.gochickfarm.ui.theme.AppTheme

@Composable
fun DashboardScreen(
    summaryUiState: DashboardViewModel.SummaryUiState,
    onNavigateToAddFeeding: () -> Unit,
    onNavigateToBreeding: () -> Unit,
    onNavigateToTransaction: () -> Unit,
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
private fun DashboardPreview() {
    AppTheme {
        DashboardScreen(
            summaryUiState = DashboardViewModel.SummaryUiState(
                summary = Summary(
                    5,
                    2,
                    7,
                    2,
                    2000000
                )
            ),
            {},
            {},
            {}
        )
    }
}