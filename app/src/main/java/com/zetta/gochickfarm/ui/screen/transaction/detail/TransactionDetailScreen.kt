package com.zetta.gochickfarm.ui.screen.transaction.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zetta.gochickfarm.R
import com.zetta.gochickfarm.ui.screen.animal.detail.DetailRow
import com.zetta.gochickfarm.utils.formatDate
import com.zetta.gochickfarm.utils.formatRupiah
import com.zetta.gochickfarm.utils.shimmerLoading
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: TransactionDetailViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState

    val statusColor = when (uiState.transaction?.type) {
        "Pemasukan" -> MaterialTheme.colorScheme.primaryContainer
        "Pengeluaran" -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surface
    }

    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(start = 4.dp, top = 4.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Detail Animal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(MaterialTheme.shapes.small)
                            .height(216.dp)
                            .shimmerLoading(),
                    )
                    Spacer(Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .width(144.dp)
                            .height(16.dp)
                            .padding(start = 16.dp)
                            .clip(MaterialTheme.shapes.small)
                            .shimmerLoading(),
                    )
                    Spacer(Modifier.height(16.dp))
                    FlowColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(2) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .shimmerLoading()
                            )
                        }
                    }
                }
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                uiState.transaction != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(
                            containerColor = statusColor
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            DetailRow("Description", uiState.transaction.description)
                            DetailRow("Type", uiState.transaction.type)
                            DetailRow("Total", formatRupiah(uiState.transaction.amount))
                            DetailRow("Date", formatDate(uiState.transaction.date))
                        }
                    }

                    if (uiState.transaction.animals.isNotEmpty()) {
                        Spacer(Modifier.height(24.dp))
                        Text(
                            text = "Animals Involved",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            overscrollEffect = null,
                            contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
                        ) {
                            items(uiState.transaction.animals, key = { it.id }) { animal ->
                                TransactionAnimalCard(
                                    tag = animal.tag,
                                    species = animal.species,
                                    status = animal.status
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionAnimalCard(
    tag: String,
    species: String,
    status: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(if (species == "Ayam") R.drawable.ic_chicken else R.drawable.ic_goat),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TAG: $tag",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}