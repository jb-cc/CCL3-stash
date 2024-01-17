package com.cc221012_cc221016.stash.ui.views.Composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.cc221012_cc221016.stash.R
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
    val focusRequester = remember { FocusRequester() }


    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Stash",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 100.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.shield),
                contentDescription = "Login Icon",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
                    .size(200.dp),
                colorFilter = ColorFilter.tint(Color.White)

            )

            Column(modifier = Modifier.weight(1f)) {
                if (user != null) {
                    var isPasswordIncorrect by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            verticalArrangement = Arrangement.Top, // Center the contents vertically
                            horizontalAlignment = Alignment.CenterHorizontally, // Center the contents horizontally
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 25.dp) // Fill the maximum size
                        ) {

                            OutlinedTextField(
                                value = password,
                                onValueChange = { newPassword ->
                                    if (newPassword.text.length <= 50) {
                                        password = newPassword
                                    }
                                },
                                label = { Text(text = "Master Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = {
                                    isPasswordIncorrect = if (password.text == user.userPassword) {
                                        viewModel.authenticateUser()
                                        false
                                    } else {
                                        true
                                    }
                                }),
                                isError = isPasswordIncorrect,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = if (isPasswordIncorrect) Color.Red else MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = if (isPasswordIncorrect) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
                                ),
                                supportingText = { if (isPasswordIncorrect) Text(text = "Incorrect Password") },
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 110.dp)
                        ) {

                            Button(
                                onClick = {
                                    if (password.text == user.userPassword) {
                                        viewModel.authenticateUser()
                                        isPasswordIncorrect = false
                                    } else {
                                        coroutineScope.launch {
                                            isPasswordIncorrect = true
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.67f)
                                    .padding(bottom = 16.dp)
                            ) {
                                Text(text = "Log In")
                            }
                        }
                    }
                } else {
                    var newPWisFocused by remember { mutableStateOf(false) }
                    var repeatPWisFocused by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = masterPassword,
                        onValueChange = { newMasterPassword -> if (newMasterPassword.text.length <= 50) masterPassword = newMasterPassword },
                        label = { Text(text = "New Master Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() }),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                newPWisFocused = focusState.isFocused
                            },
                        supportingText = { if(newPWisFocused)PasswordRequirements(masterPassword.text) }
                    )

                    OutlinedTextField(
                        value = repeatMasterPassword,
                        onValueChange = { newRepeatMasterPassword -> if (newRepeatMasterPassword.text.length <= 50) repeatMasterPassword = newRepeatMasterPassword },
                        label = { Text(text = "Repeat Master Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (isPasswordValid(masterPassword.text)) {
                                if (masterPassword.text == repeatMasterPassword.text) {
                                    val newUser = Users(masterPassword.text)
                                    viewModel.saveUser(newUser)
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
                        }),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                repeatPWisFocused = focusState.isFocused
                            },
                        isError = if(repeatPWisFocused) {
                            !isPasswordValid(repeatMasterPassword.text)
                        } else {
                            false
                        },
                        supportingText = {if (repeatPWisFocused){
                            when {
                                repeatMasterPassword.text.isEmpty() -> Text(text = "Please repeat password")
                                masterPassword.text != repeatMasterPassword.text -> Text(text = "Passwords do not match")
                            }
                        } }
                    )

                    Box {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
//                            .padding(top = 100.dp)
                        ) {

                            Button(
                                onClick = {
                                    if (isPasswordValid(masterPassword.text)) {
                                        if (masterPassword.text == repeatMasterPassword.text) {
                                            val newUser = Users(masterPassword.text)
                                            viewModel.saveUser(newUser)
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
                                    .fillMaxWidth(0.67f)
                                    .padding(bottom = 16.dp)
                            ) {
                                Text(text = "Sign Up with MasterPassword")
                            }

                        }
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PasswordRequirements(password: String, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    colorScheme.onSurface
    colorScheme.primary

    val hasMinLength = password.length >= 8
    val hasNumber = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }
    val hasUppercase = password.any { it.isUpperCase() }

    updateTransition(targetState = password, label = "transition")

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