package com.cc221012_cc221016.stash.ui.views.composables

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    var isPasswordInvalid by remember { mutableStateOf(false) }
    val context = LocalContext.current


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
                    var passwordVisibility by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            verticalArrangement = Arrangement.Top, // Center the contents vertically
                            horizontalAlignment = Alignment.CenterHorizontally, // Center the contents horizontally
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 25.dp) // Fill the maximum size
                        ) {
                            Text(
                                text = "Please log in using your Masterpassword",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            OutlinedTextField(
                                value = password,
                                singleLine = true,
                                onValueChange = { newPassword ->
                                    if (newPassword.text.length <= 50) {
                                        password = newPassword
                                    }
                                },
                                label = { Text(text = "Master Password") },
                                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
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
                                supportingText = { if (isPasswordIncorrect) Text(text = "Incorrect Password") },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                        Icon(
                                            imageVector = if (passwordVisibility) ImageVector.vectorResource(
                                                id = R.drawable.visibility_on
                                            ) else ImageVector.vectorResource(id = R.drawable.visibility_off),
                                            contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                                            tint = MaterialTheme.colorScheme.onSurface // Set the color of the icon
                                        )
                                    }
                                }
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
                                            Toast.makeText(context, "Incorrect Password", Toast.LENGTH_LONG).show()

                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.67f)
                                    .padding(top = 40.dp)
                            ) {
                                Text(text = "Log In")
                            }
                        }
                    }
                } else {
                    var newPWisFocused by remember { mutableStateOf(false) }
                    var repeatPWisFocused by remember { mutableStateOf(false) }
                    var masterPasswordVisibility by remember { mutableStateOf(false) }
                    var repeatMasterPasswordVisibility by remember { mutableStateOf(false) }


                    var showDialog by remember { mutableStateOf(true) }


                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text(text = "Getting started") },
                            text = {
                                val styledText = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)) {
                                        append("🔒 Your Privacy Comes First: \n")
                                    }
                                    append("We respect your privacy. Your data is yours alone, we do not have access to any of your data.\n\n")

                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)) {
                                        append("🔑 Create Your Master Password: \n")
                                    }
                                    append("This is the only key to your digital vault. Make it strong and unique. Remember, your master password is not stored anywhere and cannot be recovered if forgotten.\n\n")

                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)) {
                                        append("⚠️ Don't Forget: \n")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize, color = MaterialTheme.colorScheme.error)) {
                                        append("If you lose your master password, you lose access to your vault. If you uninstall the app, you lose access to your vault.")
                                    }
                                    append(" There's no way to reset it, as we don't keep a copy. This ensures that only you have access to your passwords.")
                                }

                                Text(text = styledText)
                            },
                            confirmButton = {
                                Button(onClick = { showDialog = false }) {
                                    Text("Okay")
                                }
                            }
                        )

                    }
                    Text(
                        text = "Create a Master Password to get started",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
                    )

                    OutlinedTextField(
                        value = masterPassword,
                        singleLine = true,
                        onValueChange = { newMasterPassword -> if (newMasterPassword.text.length <= 50) masterPassword = newMasterPassword },
                        label = { Text(text = "New Master Password") },
                        visualTransformation = if (masterPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() }),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                newPWisFocused = focusState.isFocused
                            },
                        trailingIcon = {
                            IconButton(onClick = { masterPasswordVisibility = !masterPasswordVisibility }) {
                                Icon(
                                    imageVector = if (masterPasswordVisibility) ImageVector.vectorResource(
                                        id = R.drawable.visibility_on) else ImageVector.vectorResource(
                                        id = R.drawable.visibility_off),
                                    contentDescription = if (masterPasswordVisibility) "Hide password" else "Show password",
                                    tint = MaterialTheme.colorScheme.onSurface // Set the color of the icon

                                )
                            }
                        },


                        isError = if(newPWisFocused || isPasswordInvalid) {
                            !isPasswordValid(masterPassword.text)
                        } else {
                            false
                        },
                        supportingText = {
                            if(newPWisFocused)PasswordRequirements(masterPassword.text) else {
                                if (isPasswordInvalid) {
                                    when {
                                        masterPassword.text.isEmpty() -> Text(text = "Please enter a password")
                                        !isPasswordValid(masterPassword.text) -> Text(text = "Password does not meet the requirements")
                                    }
                                }
                            }
                        }

                    )


                    OutlinedTextField(
                        value = repeatMasterPassword,
                        singleLine = true,
                        onValueChange = { newRepeatMasterPassword -> if (newRepeatMasterPassword.text.length <= 50) repeatMasterPassword = newRepeatMasterPassword },
                        label = { Text(text = "Repeat Master Password") },

                        visualTransformation = if (repeatMasterPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (isPasswordValid(masterPassword.text)) {
                                if (masterPassword.text == repeatMasterPassword.text) {
                                    val newUser = Users(masterPassword.text)
                                    viewModel.saveUser(newUser)
                                }
                            }
                        }),
                        trailingIcon = {
                            IconButton(onClick = { repeatMasterPasswordVisibility = !repeatMasterPasswordVisibility }) {
                                Icon(
                                    imageVector = if (repeatMasterPasswordVisibility)ImageVector.vectorResource(
                                        id = R.drawable.visibility_on
                                    ) else ImageVector.vectorResource(id = R.drawable.visibility_off),
                                    contentDescription = if (repeatMasterPasswordVisibility) "Hide password" else "Show password",
                                    tint = MaterialTheme.colorScheme.onSurface // Set the color of the icon
                                )
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                repeatPWisFocused = focusState.isFocused
                            },
                        isError = if(repeatPWisFocused || isPasswordInvalid) {
                            !isPasswordValid(repeatMasterPassword.text)
                        } else {
                            false
                        },
                        supportingText = {if (repeatPWisFocused || isPasswordInvalid){
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
                                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        isPasswordInvalid = true
                                        Toast.makeText(context, "Password does not meet the requirements", Toast.LENGTH_LONG).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.67f)
                                    .padding(bottom = 16.dp)

                            ) {
                                Text(text = "Sign Up with MasterPassword")
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { showDialog = true }
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 8.dp)
                            ) {
                                Text(
                                    text = "Help",
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline,
                                    style = MaterialTheme.typography.bodyMedium
                                )
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