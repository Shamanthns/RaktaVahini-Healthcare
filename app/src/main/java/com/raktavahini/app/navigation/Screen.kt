package com.raktavahini.app.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Registration : Screen("registration")
    object Home : Screen("home")
    object EmergencySearch : Screen("emergency_search")
    object LogDonation : Screen("log_donation")
    object DonationHistory : Screen("donation_history")
    object Profile : Screen("profile")
    object BloodGroupGuide : Screen("blood_group_guide")
}
