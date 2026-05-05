package com.raktavahini.app.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raktavahini.app.ui.components.DonorCard
import com.raktavahini.app.ui.components.RaktaTopBar
import com.raktavahini.app.ui.theme.RaktaRed
import com.raktavahini.app.ui.theme.RaktaRedContainer
import com.raktavahini.app.utils.BLOOD_GROUPS
import com.raktavahini.app.viewmodel.RaktaVahiniViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencySearchScreen(
    viewModel: RaktaVahiniViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedBloodGroup by viewModel.selectedBloodGroup.collectAsState()
    val selectedRadius by viewModel.selectedRadius.collectAsState()
    var bloodGroupExpanded by remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RaktaTopBar(title = "Emergency Search 🔍", onBack = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Controls
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RaktaRedContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Find Compatible Donors",
                        fontWeight = FontWeight.Bold,
                        color = RaktaRed,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(12.dp))

                    // Blood Group Picker
                    ExposedDropdownMenuBox(
                        expanded = bloodGroupExpanded,
                        onExpandedChange = { bloodGroupExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedBloodGroup,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Required Blood Group") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(bloodGroupExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RaktaRed,
                                focusedLabelColor = RaktaRed
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = bloodGroupExpanded,
                            onDismissRequest = { bloodGroupExpanded = false }
                        ) {
                            BLOOD_GROUPS.forEach { group ->
                                DropdownMenuItem(
                                    text = { Text(group) },
                                    onClick = {
                                        viewModel.setSelectedBloodGroup(group)
                                        bloodGroupExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Radius Selection
                    Text("Search Radius", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(10, 20, 50).forEach { radius ->
                            FilterChip(
                                selected = selectedRadius == radius,
                                onClick = { viewModel.setSelectedRadius(radius) },
                                label = { Text("${radius} km") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RaktaRed,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.searchDonors()
                            hasSearched = true
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RaktaRed)
                    ) {
                        Icon(Icons.Default.Search, null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Search Eligible Donors", fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (hasSearched) {
                if (searchResults.isEmpty()) {
                    // Empty State
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("😔", fontSize = 48.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No eligible donors found",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                            Text(
                                "Try increasing the radius or\nchoose a different blood group",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    Text(
                        "✓ ${searchResults.size} eligible donor(s) found",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        fontSize = 14.sp
                    )
                    LazyColumn {
                        items(searchResults) { donor ->
                            DonorCard(
                                donor = donor,
                                userLat = currentUser?.latitude ?: 12.9716,
                                userLng = currentUser?.longitude ?: 77.5946,
                                onCallClick = { phone ->
                                    // Privacy-safe: Use Intent.ACTION_DIAL, number not shown in UI
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            } else {
                // Idle State
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🩸", fontSize = 56.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("Select blood group and\npress Search", color = Color.Gray, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}
