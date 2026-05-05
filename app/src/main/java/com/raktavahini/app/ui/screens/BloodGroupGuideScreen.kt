package com.raktavahini.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raktavahini.app.ui.components.RaktaTopBar
import com.raktavahini.app.ui.theme.RaktaRed
import com.raktavahini.app.ui.theme.RaktaRedContainer

data class BloodGroupInfo(
    val group: String,
    val canDonateTo: String,
    val canReceiveFrom: String,
    val universalNote: String = ""
)

@Composable
fun BloodGroupGuideScreen(onBack: () -> Unit) {
    val bloodGroupData = listOf(
        BloodGroupInfo("O-", "Everyone (Universal Donor)", "O- only", "🌟 Universal Donor"),
        BloodGroupInfo("O+", "O+, A+, B+, AB+", "O+, O-", ""),
        BloodGroupInfo("A-", "A-, A+, AB-, AB+", "A-, O-", ""),
        BloodGroupInfo("A+", "A+, AB+", "A+, A-, O+, O-", ""),
        BloodGroupInfo("B-", "B-, B+, AB-, AB+", "B-, O-", ""),
        BloodGroupInfo("B+", "B+, AB+", "B+, B-, O+, O-", ""),
        BloodGroupInfo("AB-", "AB-, AB+", "AB-, A-, B-, O-", ""),
        BloodGroupInfo("AB+", "AB+ only (Universal Recipient)", "Everyone", "🌟 Universal Recipient"),
    )

    Scaffold(
        topBar = { RaktaTopBar(title = "Blood Group Guide 🩸", onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RaktaRedContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Donation Compatibility", fontWeight = FontWeight.Bold, color = RaktaRed, fontSize = 16.sp)
                        Text("Learn who can donate to whom", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }

            items(bloodGroupData.size) { index ->
                val info = bloodGroupData[index]
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = RaktaRed
                            ) {
                                Text(
                                    info.group,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                            if (info.universalNote.isNotBlank()) {
                                Spacer(Modifier.width(8.dp))
                                Text(info.universalNote, fontSize = 13.sp)
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Row {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Can Donate To", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFF2E7D32))
                                Text(info.canDonateTo, fontSize = 13.sp, color = Color.DarkGray, lineHeight = 18.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Can Receive From", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFF1565C0))
                                Text(info.canReceiveFrom, fontSize = 13.sp, color = Color.DarkGray, lineHeight = 18.sp)
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("⏱️ 90-Day Rule", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Whole blood donors must wait at least 90 days (about 3 months) between donations. This ensures your body fully replenishes the donated blood.",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
