package com.raktavahini.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.raktavahini.app.ui.theme.RaktaRed
import com.raktavahini.app.ui.theme.RaktaRedContainer
import com.raktavahini.app.ui.theme.RaktaRedDark
import kotlinx.coroutines.launch

data class OnboardingPage(
    val emoji: String,
    val title: String,
    val description: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pages = listOf(
        OnboardingPage(
            emoji = "🩸",
            title = "What is Rakta-Vahini?",
            description = "A smart blood donor network that replaces chaotic WhatsApp forwards with a filtered, privacy-aware directory of eligible donors."
        ),
        OnboardingPage(
            emoji = "🎯",
            title = "How It Works",
            description = "Search by blood group and location. Only eligible donors (90+ days since last donation) within your radius are shown — no noise, no irrelevant contacts."
        ),
        OnboardingPage(
            emoji = "🔒",
            title = "Your Privacy, Protected",
            description = "Donor phone numbers are never shown publicly. Calls are made via secure Intent dial. Your data stays on your device."
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(pages[page])
        }

        // Indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            repeat(pages.size) { index ->
                val color = if (pagerState.currentPage == index) RaktaRed else RaktaRedContainer
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (pagerState.currentPage == index) 20.dp else 8.dp, 8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage < pages.size - 1) {
                TextButton(onClick = onFinished) {
                    Text("Skip", color = RaktaRed)
                }
                Button(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RaktaRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Next →")
                }
            } else {
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = onFinished,
                    colors = ButtonDefaults.buttonColors(containerColor = RaktaRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Get Started", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(page.emoji, fontSize = 80.sp)
        Spacer(Modifier.height(32.dp))
        Text(
            text = page.title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = RaktaRedDark,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = page.description,
            fontSize = 16.sp,
            color = Color(0xFF555555),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}
