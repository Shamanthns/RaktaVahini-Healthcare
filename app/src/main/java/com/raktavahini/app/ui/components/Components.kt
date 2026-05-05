package com.raktavahini.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raktavahini.app.data.model.Donor
import com.raktavahini.app.ui.theme.*
import com.raktavahini.app.utils.EligibilityUtils
import com.raktavahini.app.utils.LocationUtils

@Composable
fun BloodGroupBadge(
    bloodGroup: String,
    modifier: Modifier = Modifier,
    size: Int = 48
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(RaktaRed)
    ) {
        Text(
            text = bloodGroup,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size / 3.5).sp
        )
    }
}

@Composable
fun EligibilityChip(isEligible: Boolean, daysLeft: Long = 0) {
    val (label, color, bg) = if (isEligible) {
        Triple("✓ Eligible", EligibleGreen, Color(0xFFE8F5E9))
    } else {
        Triple("${daysLeft}d to wait", IneligibleOrange, Color(0xFFFFF3E0))
    }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bg
    ) {
        Text(
            text = label,
            color = color,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun DonorCard(
    donor: Donor,
    userLat: Double,
    userLng: Double,
    onCallClick: (String) -> Unit
) {
    val isEligible = EligibilityUtils.isEligible(donor.lastDonationDate)
    val daysLeft = EligibilityUtils.daysUntilEligible(donor.lastDonationDate)
    val distanceKm = LocationUtils.haversineDistance(userLat, userLng, donor.latitude, donor.longitude)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BloodGroupBadge(bloodGroup = donor.bloodGroup, size = 52)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = donor.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${donor.city} · ${"%.1f".format(distanceKm)} km",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                EligibilityChip(isEligible = isEligible, daysLeft = daysLeft)
            }
            if (isEligible) {
                IconButton(
                    onClick = { onCallClick(donor.phone) },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(RaktaRed)
                ) {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = "Call Donor",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = RaktaRed,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaktaTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = RaktaRed)
    )
}

@Composable
fun StatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RaktaRedContainer),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(icon, contentDescription = null, tint = RaktaRed, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = RaktaRedDark)
            Text(label, fontSize = 12.sp, color = TextSecondary)
        }
    }
}
