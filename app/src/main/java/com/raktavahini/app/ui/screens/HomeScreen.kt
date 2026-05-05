package com.raktavahini.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raktavahini.app.data.model.Donor
import com.raktavahini.app.ui.components.BloodGroupBadge
import com.raktavahini.app.ui.components.EligibilityChip
import com.raktavahini.app.ui.components.StatCard
import com.raktavahini.app.ui.theme.*
import com.raktavahini.app.utils.EligibilityUtils
import com.raktavahini.app.viewmodel.RaktaVahiniViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: RaktaVahiniViewModel,
    onSearchClick: () -> Unit,
    onLogDonation: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit,
    onBloodGuide: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val donationLogs by viewModel.donationLogs.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.Default.Logout, null, tint = RaktaRed) },
            title = { Text("Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout from Rakta-Vahini?") },
            confirmButton = {
                Button(
                    onClick = { showLogoutDialog = false; onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = RaktaRed)
                ) { Text("Logout") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Rakta-Vahini 🩸", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("रक्त-वाहिनी", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }
                },
                actions = {
                    IconButton(onClick = onProfile) {
                        Icon(Icons.Default.Person, "Profile", tint = Color.White)
                    }
                    TextButton(onClick = { showLogoutDialog = true }) {
                        Text("Logout", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RaktaRed)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            currentUser?.let { user ->
                UserStatusCard(user = user, onToggle = { viewModel.toggleAvailability() })
            }

            Spacer(Modifier.height(8.dp))

            currentUser?.let { user ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = "Donations",
                        value = donationLogs.size.toString(),
                        icon = Icons.Default.Favorite,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Blood Group",
                        value = user.bloodGroup,
                        icon = Icons.Default.LocalHospital,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "City",
                        value = user.city.take(6),
                        icon = Icons.Default.LocationOn,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "Quick Actions",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = RaktaRedDark
            )
            Spacer(Modifier.height(12.dp))

            QuickActionCard("🔍", "Emergency Search", "Find compatible donors nearby", onSearchClick)
            QuickActionCard("📝", "Log a Donation", "Record your donation today", onLogDonation)
            QuickActionCard("📋", "Donation History", "View your past donations", onHistory)
            QuickActionCard("🩸", "Blood Group Guide", "Who can donate to whom", onBloodGuide)

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F0))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("💡", fontSize = 24.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Did you know?", fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                        Text(
                            "One blood donation can save up to 3 lives. Donors must wait 90 days between whole blood donations.",
                            fontSize = 13.sp,
                            color = Color(0xFF555555),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun UserStatusCard(user: Donor, onToggle: () -> Unit) {
    val isEligible = EligibilityUtils.isEligible(user.lastDonationDate)
    val daysLeft = EligibilityUtils.daysUntilEligible(user.lastDonationDate)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(RaktaRed, RaktaRedDark)))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BloodGroupBadge(bloodGroup = user.bloodGroup, size = 56)
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Welcome, ${user.name.split(" ").first()}!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(user.city, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    EligibilityChip(isEligible = isEligible, daysLeft = daysLeft)
                }
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("I am Ready to Donate", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Text(
                        if (user.isAvailable) "Visible to seekers" else "Hidden from search",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
                Switch(
                    checked = user.isAvailable,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = EligibleGreen)
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(icon: String, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 28.sp)
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(subtitle, fontSize = 13.sp, color = TextSecondary)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = RaktaRed)
        }
    }
}
