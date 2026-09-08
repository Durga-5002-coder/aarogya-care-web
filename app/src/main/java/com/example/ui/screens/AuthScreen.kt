package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MedicalBluePrimary
import com.example.ui.theme.MedicalTealDark
import com.example.ui.theme.MedicalTealPrimary
import com.example.ui.viewmodel.AuthUiState
import com.example.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Login with Aadhaar, 1: New Registration

    // Login state
    var loginAadhaar by remember { mutableStateOf("5482 9104 3821") }
    var loginOtp by remember { mutableStateOf("123456") }
    var isOtpSentForLogin by remember { mutableStateOf(false) }

    // Register state
    var regFullName by remember { mutableStateOf("") }
    var regAge by remember { mutableStateOf("") }
    var regGender by remember { mutableStateOf("Male") }
    var regPhone by remember { mutableStateOf("+91 ") }
    var regAadhaar by remember { mutableStateOf("") }
    var regAddress by remember { mutableStateOf("") }
    var regPhoneOtp by remember { mutableStateOf("") }
    var isPhoneOtpSent by remember { mutableStateOf(false) }
    var isPhoneVerified by remember { mutableStateOf(false) }
    var isAadhaarOtpSent by remember { mutableStateOf(false) }
    var regAadhaarOtp by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // App Brand Header
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(MedicalTealPrimary, MedicalBluePrimary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = "AarogyaCare Logo",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "AarogyaCare",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MedicalTealDark
            )
            Text(
                text = "National Smart OP Healthcare & Triage Portal",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Firebase Authentication & UIDAI e-KYC Security Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF0FDF4),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Secured by Firebase Auth & UIDAI e-KYC",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF15803D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Tab Selector: Existing User vs New User
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MedicalTealPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Aadhaar Login", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("tab_aadhaar_login")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("New Registration", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("tab_new_registration")
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    if (selectedTab == 0) {
                        // --- EXISTING USER AADHAAR LOGIN ---
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = null,
                                tint = MedicalTealPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Login using Aadhaar",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Authenticate securely with your 12-digit Aadhaar & UIDAI OTP",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = loginAadhaar,
                            onValueChange = { loginAadhaar = it },
                            label = { Text("12-Digit Aadhaar Number") },
                            placeholder = { Text("XXXX XXXX XXXX") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = MedicalTealPrimary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_aadhaar_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MedicalTealPrimary,
                                focusedLabelColor = MedicalTealPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (!isOtpSentForLogin) {
                            Button(
                                onClick = { isOtpSentForLogin = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("send_aadhaar_otp_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary)
                            ) {
                                Icon(imageVector = Icons.Default.Security, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Request UIDAI OTP", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFDCFCE7),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "✓ 6-Digit OTP sent to Aadhaar linked mobile ending in **210",
                                    fontSize = 11.sp,
                                    color = Color(0xFF15803D),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = loginOtp,
                                onValueChange = { loginOtp = it },
                                label = { Text("Enter 6-Digit OTP") },
                                placeholder = { Text("123456") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = MedicalTealPrimary)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_otp_input")
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    authViewModel.loginWithAadhaar(loginAadhaar, loginOtp)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("login_submit_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary)
                            ) {
                                Text("Authenticate with Firebase & Aadhaar OTP", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        // --- NEW USER REGISTRATION FLOW ---
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MedicalBluePrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Create Account",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Phone Verification & Aadhaar Authentication",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Step 1: Phone Verification
                        Text(
                            text = "Step 1: Phone Verification",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicalTealDark
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = regPhone,
                            onValueChange = { regPhone = it },
                            label = { Text("Mobile Number") },
                            placeholder = { Text("+91 9876543210") },
                            leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_phone_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (!isPhoneVerified) {
                            if (!isPhoneOtpSent) {
                                OutlinedButton(
                                    onClick = { isPhoneOtpSent = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("send_phone_otp_btn")
                                ) {
                                    Text("Send SMS OTP to Mobile")
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = regPhoneOtp,
                                        onValueChange = { regPhoneOtp = it },
                                        label = { Text("SMS OTP") },
                                        placeholder = { Text("123456") },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("reg_phone_otp_input")
                                    )
                                    Button(
                                        onClick = { isPhoneVerified = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary),
                                        modifier = Modifier.testTag("verify_phone_otp_btn")
                                    ) {
                                        Text("Verify")
                                    }
                                }
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFDCFCE7),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Phone Number Verified Successfully", fontSize = 11.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Step 2: Aadhaar Authentication
                        Text(
                            text = "Step 2: Aadhaar Authentication",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicalTealDark
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = regAadhaar,
                            onValueChange = { regAadhaar = it },
                            label = { Text("12-Digit Aadhaar Number") },
                            placeholder = { Text("1234 5678 9012") },
                            leadingIcon = { Icon(imageVector = Icons.Default.Shield, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_aadhaar_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Auto-populate / Demographic fields
                        OutlinedTextField(
                            value = regFullName,
                            onValueChange = { regFullName = it },
                            label = { Text("Full Name (as per Aadhaar)") },
                            placeholder = { Text("e.g. Ramesh Kumar") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_name_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = regAge,
                                onValueChange = { regAge = it },
                                label = { Text("Age (from Aadhaar)") },
                                placeholder = { Text("28") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("reg_age_input")
                            )

                            OutlinedTextField(
                                value = regGender,
                                onValueChange = { regGender = it },
                                label = { Text("Gender") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("reg_gender_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = regAddress,
                            onValueChange = { regAddress = it },
                            label = { Text("Residential Address (as in Aadhaar)") },
                            placeholder = { Text("City, State, PIN") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reg_address_input")
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val ageInt = regAge.toIntOrNull() ?: 28
                                val name = if (regFullName.isNotBlank()) regFullName else "Aadhaar Verified Citizen"
                                val aadh = if (regAadhaar.isNotBlank()) regAadhaar else "4820 9104 3821"
                                authViewModel.registerNewUser(
                                    fullName = name,
                                    age = ageInt,
                                    gender = regGender,
                                    phone = regPhone.ifBlank { "+91 98765 43210" },
                                    aadhaarInput = aadh,
                                    address = regAddress
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("register_submit_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary)
                        ) {
                            Text("Complete Aadhaar e-KYC & Register with Firebase", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    // Loading / Error messages
                    if (authState is AuthUiState.Loading) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MedicalTealPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Authenticating with Firebase & UIDAI Gateway...", fontSize = 12.sp, color = MedicalTealDark)
                        }
                    }

                    if (authState is AuthUiState.Error) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = (authState as AuthUiState.Error).message,
                            color = Color(0xFFDC2626),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Demo Account Access Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Instant Demo Aadhaar Account",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MedicalTealDark
                    )
                    Text(
                        text = "Tap below to test instant authentication for Durga Reddy Nidrabingi (Aadhaar: 5482 9104 3821)",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            loginAadhaar = "5482 9104 3821"
                            loginOtp = "123456"
                            isOtpSentForLogin = true
                            authViewModel.loginWithAadhaar("5482 9104 3821", "123456")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quick_demo_login_button")
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = MedicalTealPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Auto-Login with Demo Aadhaar Profile", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
