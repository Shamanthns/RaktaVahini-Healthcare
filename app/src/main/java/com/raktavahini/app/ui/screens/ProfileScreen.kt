package com.raktavahini.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raktavahini.app.ui.components.BloodGroupBadge
import com.raktavahini.app.ui.components.EligibilityChip
import com.raktavahini.app.ui.components.RaktaTopBar
import com.raktavahini.app.ui.theme.RaktaRed
import com.raktavahini.app.ui.theme.RaktaRedContainer
import com.raktavahini.app.ui.theme.RaktaRedDark
import com.raktavahini.app.utils.BLOOD_GROUPS
import com.raktavahini.app.utils.EligibilityUtils
import com.raktavahini.app.viewmodel.RaktaVahiniViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: RaktaVahiniViewModel,
    onBack: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val donationLogs by viewModel.donationLogs.collectAsState()

    var editMode by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    var editCity by remember { mutableStateOf("") }
    var editBloodGroup by remember { mutableStateOf("O+") }
    var bloodGroupExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser, editMode) {
        if (editMode && currentUser != null) {
            editName = currentUser!!.name
            editCity = currentUser!!.city
            editBloodGroup = currentUser!!.bloodGroup
        }
    }

    Scaffold(
        topBar = {
            RaktaTopBar(
                title = "My Profile",
                onBack = onBack,
                actions = {
                    if (currentUser != null) {
                        TextButton(onClick = {
                            if (editMode) {
                                viewModel.updateProfile(editName, editBloodGroup, editCity)
                            }
                            editMode = !editMode
                        }) {
                            Text(if (editMode) "Save" else "Edit", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }
    ) { padding ->
        currentUser?.let { user ->
            val isEligible = EligibilityUtils.isEligible(user.lastDonationDate)
            val daysLeft = EligibilityUtils.daysUntilEligible(user.lastDonationDate)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Avatar + Name
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BloodGroupBadge(bloodGroup = user.bloodGroup, size = 80)
                    Spacer(Modifier.height(8.dp))
                    Text(user.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text(user.city, color = Color.Gray, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    EligibilityChip(isEligible = isEligible, daysLeft = daysLeft)
                }

                HorizontalDivider()

                if (editMode) {
                    // Edit Fields
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed)
                    )
                    OutlinedTextField(
                        value = editCity,
                        onValueChange = { editCity = it },
                        label = { Text("City") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed)
                    )
                    ExposedDropdownMenuBox(
                        expanded = bloodGroupExpanded,
                        onExpandedChange = { bloodGroupExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = editBloodGroup,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Blood Group") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(bloodGroupExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed)
                        )
                        ExposedDropdownMenu(expanded = bloodGroupExpanded, onDismissRequest = { bloodGroupExpanded = false }) {
                            BLOOD_GROUPS.forEach { group ->
                                DropdownMenuItem(text = { Text(group) }, onClick = { editBloodGroup = group; bloodGroupExpanded = false })
                            }
                        }
                    }
                } else {
                    // Info Cards
                    ProfileInfoRow(label = "Phone", value = "●●●●●●${user.phone.takeLast(4)}", icon = "📱")
                    ProfileInfoRow(label = "Blood Group", value = user.bloodGroup, icon = "🩸")
                    ProfileInfoRow(label = "City", value = user.city, icon = "📍")
                    ProfileInfoRow(
                        label = "Last Donation",
                        value = EligibilityUtils.formatDate(user.lastDonationDate),
                        icon = "📅"
                    )
                    ProfileInfoRow(
                        label = "Total Donations",
                        value = "${donationLogs.size}",
                        icon = "❤️"
                    )
                    ProfileInfoRow(
                        label = "Lives Impacted",
                        value = "${donationLogs.size * 3}+",
                        icon = "🌟"
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String, icon: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 20.sp)
            Spacer(Modifier.width(12.dp))
            Text(label, color = Color.Gray, modifier = Modifier.weight(1f), fontSize = 14.sp)
            Text(value, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        }
    }
}
