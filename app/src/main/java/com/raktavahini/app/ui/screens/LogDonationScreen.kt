package com.raktavahini.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raktavahini.app.ui.components.RaktaTopBar
import com.raktavahini.app.ui.theme.EligibleGreen
import com.raktavahini.app.ui.theme.RaktaRed
import com.raktavahini.app.ui.theme.RaktaRedContainer
import com.raktavahini.app.viewmodel.RaktaVahiniViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun LogDonationScreen(
    viewModel: RaktaVahiniViewModel,
    onBack: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val donationLogs by viewModel.donationLogs.collectAsState()
    var notes by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf(LocalDate.now().toString()) }
    var dateError by remember { mutableStateOf(false) }
    var donationLogged by remember { mutableStateOf(false) }
    var alreadyLoggedToday by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Check if already logged today
    LaunchedEffect(donationLogs) {
        val today = LocalDate.now()
        alreadyLoggedToday = donationLogs.any { log ->
            val logDate = Instant.ofEpochMilli(log.donationDate)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            logDate == today
        }
    }

    Scaffold(
        topBar = { RaktaTopBar(title = "Log Donation 📝", onBack = onBack) }
    ) { padding ->
        if (donationLogged) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text("🎉", fontSize = 80.sp)
                    Spacer(Modifier.height(16.dp))
                    Text("Donation Logged!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = EligibleGreen)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Thank you for saving a life! Your 90-day cooldown has started. A notification has been sent.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(Modifier.height(32.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = RaktaRed),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Back to Home", fontWeight = FontWeight.Bold) }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RaktaRedContainer)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🩸", fontSize = 36.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Record your donation", fontWeight = FontWeight.Bold, color = RaktaRed, fontSize = 16.sp)
                            Text("Blood Group: ${currentUser?.bloodGroup ?: ""}", fontSize = 13.sp, color = Color(0xFF666666))
                        }
                    }
                }

                // Already logged today warning
                if (alreadyLoggedToday) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, null, tint = Color.Red, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "You have already logged a donation today. You cannot log another donation on the same day.",
                                fontSize = 13.sp,
                                color = Color.Red,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it; dateError = false },
                    label = { Text("Donation Date (YYYY-MM-DD)") },
                    leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                    isError = dateError,
                    supportingText = if (dateError) ({ Text("Enter a valid date (not in the future)") }) else null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed),
                    enabled = !alreadyLoggedToday
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    leadingIcon = { Icon(Icons.Default.Notes, null) },
                    placeholder = { Text("e.g., Donated at City Blood Bank") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed),
                    enabled = !alreadyLoggedToday
                )

                Spacer(Modifier.height(4.dp))

                Button(
                    onClick = {
                        if (alreadyLoggedToday) return@Button
                        val date = try { LocalDate.parse(dateText) } catch (e: Exception) { dateError = true; null }
                        if (date != null && !date.isAfter(LocalDate.now())) {
                            isSubmitting = true
                            viewModel.logDonation(notes, date)
                            donationLogged = true
                        } else if (date != null) {
                            dateError = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (alreadyLoggedToday) Color.Gray else RaktaRed
                    ),
                    enabled = !alreadyLoggedToday && !isSubmitting
                ) {
                    Icon(Icons.Default.Favorite, null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Log Donation", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Info, null, tint = Color(0xFFF57F17), modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "A 'Thank You' notification will appear, and your 90-day eligibility cooldown will begin.",
                            fontSize = 13.sp,
                            color = Color(0xFF5D4037),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
