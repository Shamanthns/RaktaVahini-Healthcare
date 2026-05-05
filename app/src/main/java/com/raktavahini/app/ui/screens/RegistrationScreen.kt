package com.raktavahini.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raktavahini.app.ui.theme.RaktaRed
import com.raktavahini.app.ui.theme.RaktaRedContainer
import com.raktavahini.app.utils.BLOOD_GROUPS
import com.raktavahini.app.viewmodel.RegisterState
import com.raktavahini.app.viewmodel.RaktaVahiniViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: RaktaVahiniViewModel,
    onRegistered: () -> Unit,
    onBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf("O+") }
    var bloodGroupExpanded by remember { mutableStateOf(false) }
    var neverDonated by remember { mutableStateOf(true) }
    var lastDonationText by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var cityError by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        if (registerState is RegisterState.Success) {
            viewModel.resetRegisterState()
            onRegistered()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Account", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RaktaRedContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🩸", fontSize = 32.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Join the network", fontWeight = FontWeight.Bold, color = RaktaRed)
                        Text("Register to save lives", fontSize = 13.sp, color = Color(0xFF666666))
                    }
                }
            }

            // Error from server (e.g. duplicate phone)
            if (registerState is RegisterState.Error) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, null, tint = Color.Red, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text((registerState as RegisterState.Error).message, color = Color.Red, fontSize = 13.sp)
                    }
                }
            }

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = "" },
                label = { Text("Full Name *") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                isError = nameError.isNotEmpty(),
                supportingText = if (nameError.isNotEmpty()) ({ Text(nameError) }) else null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed)
            )

            // Phone
            OutlinedTextField(
                value = phone,
                onValueChange = { if (it.length <= 10) { phone = it; phoneError = "" } },
                label = { Text("Phone Number *") },
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneError.isNotEmpty(),
                supportingText = if (phoneError.isNotEmpty()) ({ Text(phoneError) }) else null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed)
            )

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = "" },
                label = { Text("Password *") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color.Gray)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError.isNotEmpty(),
                supportingText = if (passwordError.isNotEmpty()) ({ Text(passwordError) }) else null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed)
            )

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; confirmPasswordError = "" },
                label = { Text("Confirm Password *") },
                leadingIcon = { Icon(Icons.Default.LockOpen, null) },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color.Gray)
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = confirmPasswordError.isNotEmpty(),
                supportingText = if (confirmPasswordError.isNotEmpty()) ({ Text(confirmPasswordError) }) else null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed)
            )

            // City
            OutlinedTextField(
                value = city,
                onValueChange = { city = it; cityError = "" },
                label = { Text("City *") },
                leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                isError = cityError.isNotEmpty(),
                supportingText = if (cityError.isNotEmpty()) ({ Text(cityError) }) else null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed)
            )

            // Blood Group Dropdown
            ExposedDropdownMenuBox(
                expanded = bloodGroupExpanded,
                onExpandedChange = { bloodGroupExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedBloodGroup,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Blood Group *") },
                    leadingIcon = { Text("🩸", modifier = Modifier.padding(start = 4.dp)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(bloodGroupExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed)
                )
                ExposedDropdownMenu(
                    expanded = bloodGroupExpanded,
                    onDismissRequest = { bloodGroupExpanded = false }
                ) {
                    BLOOD_GROUPS.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group) },
                            onClick = { selectedBloodGroup = group; bloodGroupExpanded = false }
                        )
                    }
                }
            }

            // Last Donation
            Text("Last Donation", fontWeight = FontWeight.SemiBold, color = RaktaRed)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = neverDonated,
                    onCheckedChange = { neverDonated = it },
                    colors = CheckboxDefaults.colors(checkedColor = RaktaRed)
                )
                Text("I have never donated before")
            }

            if (!neverDonated) {
                OutlinedTextField(
                    value = lastDonationText,
                    onValueChange = { lastDonationText = it },
                    label = { Text("Last donation date (YYYY-MM-DD)") },
                    leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                    placeholder = { Text("e.g. 2024-10-15") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RaktaRed, focusedLabelColor = RaktaRed)
                )
            }

            Spacer(Modifier.height(4.dp))

            // Register Button
            Button(
                onClick = {
                    nameError = if (name.isBlank()) "Name is required" else ""
                    phoneError = if (phone.length != 10) "Enter valid 10-digit number" else ""
                    passwordError = if (password.length < 6) "Password must be at least 6 characters" else ""
                    confirmPasswordError = if (password != confirmPassword) "Passwords do not match" else ""
                    cityError = if (city.isBlank()) "City is required" else ""

                    if (nameError.isEmpty() && phoneError.isEmpty() && passwordError.isEmpty()
                        && confirmPasswordError.isEmpty() && cityError.isEmpty()
                    ) {
                        val date: LocalDate? = if (!neverDonated && lastDonationText.isNotBlank()) {
                            try { LocalDate.parse(lastDonationText) } catch (e: Exception) { null }
                        } else null
                        viewModel.registerUser(name.trim(), phone.trim(), password.trim(), selectedBloodGroup, city.trim(), date)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RaktaRed),
                enabled = registerState !is RegisterState.Loading
            ) {
                if (registerState is RegisterState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Favorite, null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Register as Donor", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
