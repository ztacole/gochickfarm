package com.zetta.gochickfarm.ui.screen.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityHistoryScreen(
    onNavigateBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Semua") }

    val activities = remember {
        listOf(
            ActivityItem("12 Okt 2025", "Pakan", "KM-004 - 1.2kg Jagung"),
            ActivityItem("12 Okt 2025", "Breeding", "KM-001 x KM-014 (2 anak)"),
            ActivityItem("11 Okt 2025", "Transaksi", "Penjualan Kambing"),
            ActivityItem("11 Okt 2025", "Pakan", "AY-001 - 0.8kg Konsentrat"),
            ActivityItem("10 Okt 2025", "Breeding", "KM-003 x KM-007 (1 anak)"),
            ActivityItem("10 Okt 2025", "Transaksi", "Pembelian Pakan"),
            ActivityItem("09 Okt 2025", "Pakan", "KM-002 - 1.5kg Rumput"),
            ActivityItem("09 Okt 2025", "Transaksi", "Penjualan Ayam")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Aktivitas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = when (selectedFilter) {
                    "Semua" -> 0
                    "Pakan" -> 1
                    "Breeding" -> 2
                    "Transaksi" -> 3
                    else -> 0
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("Semua", "Pakan", "Breeding", "Transaksi").forEach { filter ->
                    Tab(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        text = { Text(filter) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(activities) { activity ->
                    ActivityCard(
                        date = activity.date,
                        type = activity.type,
                        description = activity.description
                    )
                }
            }
        }
    }
}

data class ActivityItem(
    val date: String,
    val type: String,
    val description: String
)

@Composable
fun ActivityCard(date: String, type: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (type) {
                    "Pakan" -> Icons.Rounded.Restaurant
                    "Breeding" -> Icons.Rounded.Pets
                    else -> Icons.Rounded.MonetizationOn
                },
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$type: $description",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}