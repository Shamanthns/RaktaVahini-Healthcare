package com.raktavahini.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raktavahini.app.ui.theme.RaktaRed
import com.raktavahini.app.ui.theme.RaktaRedContainer
import com.raktavahini.app.ui.theme.RaktaRedDark
import com.raktavahini.app.viewmodel.LoginState
import com.raktavahini.app.viewmodel.RaktaVahiniViewModel

@Composable
fun LoginScreen(
    viewModel: RaktaVahiniViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            viewModel.resetLoginState()
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(RaktaRedDark, RaktaRed, Color(0xFF1A0000))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(60.dp))

            // Logo & Title
            Text("🩸", fontSize = 72.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "Rakta-Vahini",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "रक्त-वाहिनी",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                "Filtered Blood Donor Network",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(40.dp))

            // Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Welcome Back",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = RaktaRed
                    )
                    Text(
                        "Login to your donor account",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    // Phone
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { if (it.length <= 10) { phone = it; phoneError = "" } },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = RaktaRed) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError.isNotEmpty(),
                        supportingText = if (phoneError.isNotEmpty()) ({ Text(phoneError, color = MaterialTheme.colorScheme.error) }) else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RaktaRed,
                            focusedLabelColor = RaktaRed
                        )
                    )

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = "" },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = RaktaRed) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = passwordError.isNotEmpty(),
                        supportingText = if (passwordError.isNotEmpty()) ({ Text(passwordError, color = MaterialTheme.colorScheme.error) }) else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RaktaRed,
                            focusedLabelColor = RaktaRed
                        )
                    )

                    // Error from ViewModel
                    if (loginState is LoginState.Error) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Error, null, tint = Color.Red, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    (loginState as LoginState.Error).message,
                                    color = Color.Red,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    // Login Button
                    Button(
                        onClick = {
                            phoneError = if (phone.length != 10) "Enter valid 10-digit number" else ""
                            passwordError = if (password.isBlank()) "Password cannot be empty" else ""
                            if (phoneError.isEmpty() && passwordError.isEmpty()) {
                                viewModel.login(phone, password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RaktaRed),
                        enabled = loginState !is LoginState.Loading
                    ) {
                        if (loginState is LoginState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Login, null, tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Log In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    HorizontalDivider(color = Color.LightGray)

                    // Register Button
                    OutlinedButton(
                        onClick = onRegisterClick,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RaktaRed),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, RaktaRed)
                    ) {
                        Icon(Icons.Default.PersonAdd, null, tint = RaktaRed)
                        Spacer(Modifier.width(8.dp))
                        Text("New user? Register here", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = RaktaRed)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                "Save a life. Donate blood. 🩸",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(40.dp))
        }
    }
}
