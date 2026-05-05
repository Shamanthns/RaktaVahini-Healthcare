package com.raktavahini.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raktavahini.app.data.model.DonationLog
import com.raktavahini.app.ui.components.RaktaTopBar
import com.raktavahini.app.ui.theme.RaktaRed
import com.raktavahini.app.ui.theme.RaktaRedContainer
import com.raktavahini.app.utils.EligibilityUtils
import com.raktavahini.app.viewmodel.RaktaVahiniViewModel

@Composable
fun DonationHistoryScreen(
    viewModel: RaktaVahiniViewModel,
    onBack: () -> Unit
) {
    val logs by viewModel.donationLogs.collectAsState()

    Scaffold(
        topBar = { RaktaTopBar(title = "Donation History 📋", onBack = onBack) }
    ) { padding ->
        if (logs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📋", fontSize = 56.sp)
                    Spacer(Modifier.height(16.dp))
                    Text("No donations yet", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(
                        "Log your first donation to\nsee your history here",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    // Summary Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = RaktaRedContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🏆", fontSize = 36.sp)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    "${logs.size} Total Donations",
                                    fontWeight = FontWeight.Bold,
                                    color = RaktaRed,
                                    fontSize = 18.sp
                                )
                                Text(
                                    "You've potentially saved ${logs.size * 3}+ lives",
                                    color = Color(0xFF666666),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                itemsIndexed(logs) { index, log ->
                    DonationLogItem(log = log, number = logs.size - index)
                }

                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun DonationLogItem(log: DonationLog, number: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Timeline indicator
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            ) {
                Surface(
                    shape = CircleShape,
                    color = RaktaRed,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "#$number",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.width(12.dp))

        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        EligibilityUtils.formatDate(log.donationDate),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RaktaRedContainer
                    ) {
                        Text(
                            log.bloodGroup,
                            color = RaktaRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                if (log.notes.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        log.notes,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, null, tint = RaktaRed, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Donation recorded", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}
