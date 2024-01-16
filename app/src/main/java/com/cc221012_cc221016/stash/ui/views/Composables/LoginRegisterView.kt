package com.cc221012_cc221016.stash.ui.views.Composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.cc221012_cc221016.stash.data.Users
import com.cc221012_cc221016.stash.models.MainViewModel
import kotlinx.coroutines.launch


fun isPasswordValid(password: String): Boolean {
    val passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$".toRegex()
    return passwordRegex.matches(password)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRegisterView(user: Users?, viewModel: MainViewModel) {
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var masterPassword by remember { mutableStateOf(TextFieldValue("")) }
    var repeatMasterPassword by remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Stash",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 10.dp)
            )

            Column(modifier = Modifier.padding(top = 200.dp)) {
                if (user != null) {
                    if (user.userID != 0) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = { newPassword -> if (newPassword.text.length <= 50) password = newPassword },
                            label = { Text(text = "Master Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        OutlinedTextField(
                            value = masterPassword,
                            onValueChange = { newMasterPassword -> if (newMasterPassword.text.length <= 50) masterPassword = newMasterPassword },
                            label = { Text(text = "New MasterPassword") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            supportingText = { PasswordRequirements(masterPassword.text) }
                        )

                        OutlinedTextField(
                            value = repeatMasterPassword,
                            onValueChange = { newRepeatMasterPassword -> if (newRepeatMasterPassword.text.length <= 50) repeatMasterPassword = newRepeatMasterPassword },
                            label = { Text(text = "Repeat Master Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Column(
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            SnackbarHost(
                                hostState = snackbarHostState
                            )
                            Button(
                                onClick = {
                                    if (isPasswordValid(masterPassword.text)) {
                                        if (masterPassword.text == repeatMasterPassword.text) {
                                            viewModel.saveUser(Users(masterPassword.text))
                                        } else {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Passwords do not match")
                                            }
                                        }
                                    } else {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Password does not meet the requirements")
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(bottom = 16.dp)
                            ) {
                                Text(text = "Sign Up with MasterPassword")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PasswordRequirements(password: String, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val defaultColor = colorScheme.onSurface
    val primaryColor = colorScheme.primary

    val hasMinLength = password.length >= 8
    val hasNumber = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }
    val hasUppercase = password.any { it.isUpperCase() }

    val transition = updateTransition(targetState = password, label = "transition")

    val color: Color by animateColorAsState(targetValue = MaterialTheme.colorScheme.onSurface,
        label = ""
    )


    Column(modifier = modifier) {
        Crossfade(targetState = !hasMinLength, animationSpec = tween(durationMillis = 1000),
            label = ""
        ) { isVisible ->
            if (isVisible) {
                RequirementItem(text = "At least 8 characters", requirementMet = hasMinLength, color = color)
            }
        }

        Crossfade(targetState = !hasNumber, animationSpec = tween(durationMillis = 1000),
            label = ""
        ) { isVisible ->
            if (isVisible) {
                RequirementItem(text = "At least one number", requirementMet = hasNumber, color = color)
            }
        }

        Crossfade(targetState = !hasSpecialChar, animationSpec = tween(durationMillis = 1000),
            label = ""
        ) { isVisible ->
            if (isVisible) {
                RequirementItem(text = "At least one special character", requirementMet = hasSpecialChar, color = color)
            }
        }

        Crossfade(targetState = !hasUppercase, animationSpec = tween(durationMillis = 1000),
            label = ""
        ) { isVisible ->
            if (isVisible) {
                RequirementItem(text = "At least one uppercase letter", requirementMet = hasUppercase, color = color)
            }
        }
    }
}
@Composable
fun RequirementItem(text: String, requirementMet: Boolean, color: Color) {
    val icon = if (requirementMet) Icons.Filled.Check else Icons.Filled.Close
    val displayColor = if (requirementMet) MaterialTheme.colorScheme.primary else color

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = displayColor
        )
        Text(
            text = text,
            color = displayColor,
            style = MaterialTheme.typography.bodySmall
        )
    }
}