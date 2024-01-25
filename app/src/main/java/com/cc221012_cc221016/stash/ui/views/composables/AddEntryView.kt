package com.cc221012_cc221016.stash.ui.views.composables

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.cc221012_cc221016.stash.R
import com.cc221012_cc221016.stash.data.Entries
import com.cc221012_cc221016.stash.models.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryView(
    mainViewModel: MainViewModel,
    onBack: () -> Unit,
    navigateToShowEntry: (Entries) -> Unit
) {
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val nameValue = rememberSaveable { mutableStateOf("") }
    val urlValue = rememberSaveable { mutableStateOf("") }
    val emailValue = rememberSaveable { mutableStateOf("") }
    val passwordValue = rememberSaveable { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isNameEmpty by remember { mutableStateOf(false) }
    var isEmailEmpty by remember { mutableStateOf(false) }
    var isPasswordEmpty by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val nameFocusRequester = remember { FocusRequester() }
    val urlFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val colorScheme = colorScheme
    BackHandler {
        onBack()
    }

    MaterialTheme(colorScheme = colorScheme){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top

                ){
                    TopAppBar(
                        title = { Text("Add Entry") },
                        navigationIcon = {
                            IconButton(onClick = { onBack() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 50.dp)
                    .padding(bottom = 50.dp)
                    .verticalScroll(rememberScrollState()),
            ) {



                OutlinedTextField(
                    value = nameValue.value,
                    onValueChange = { nameValue.value = it },
                    isError = isNameEmpty,
                    supportingText = { if (isNameEmpty) Text("Required") },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth().focusRequester(nameFocusRequester),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.star),
                            contentDescription = "Star Icon"
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { urlFocusRequester.requestFocus() }),
                )

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = urlValue.value,
                    onValueChange = { urlValue.value = it },
                    label = { Text("URL") },
                    modifier = Modifier.fillMaxWidth().focusRequester(urlFocusRequester),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.link),
                            contentDescription = "URL Icon"
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { emailFocusRequester.requestFocus() }),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = emailValue.value,
                    onValueChange = { emailValue.value = it },
                    isError = isEmailEmpty,
                    supportingText = { if (isEmailEmpty) Text("Required") },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth().focusRequester(emailFocusRequester),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Email,
                            contentDescription = "Email Icon"
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),

                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = passwordValue.value,
                    onValueChange = { passwordValue.value = it },
                    isError = isPasswordEmpty,
                    supportingText = { if (isPasswordEmpty) Text("Required") },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth().focusRequester(passwordFocusRequester),
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.key),
                            contentDescription = "Key Icon"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }) {
                            Icon(
                                imageVector = if (passwordVisibility.value) ImageVector.vectorResource(
                                    id = R.drawable.visibility_on
                                ) else ImageVector.vectorResource(id = R.drawable.visibility_off),
                                contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ){
                SnackbarHost(
                    hostState = snackbarHostState,
                )
                Button(
                    onClick = {
                        if (nameValue.value.isEmpty() || emailValue.value.isEmpty() || passwordValue.value.isEmpty()) {
//                        scope.launch {
//                            snackbarHostState.showSnackbar("Please fill out all mandatory fields.")
//                        }
                            isNameEmpty = nameValue.value.isEmpty()
                            isEmailEmpty = emailValue.value.isEmpty()
                            isPasswordEmpty = passwordValue.value.isEmpty()
                        } else {
                            scope.launch {
                                try {
                                    val newEntry = Entries(
                                        entryName = nameValue.value,
                                        entryUsername = emailValue.value,
                                        entryPassword = passwordValue.value,
                                        entryUrl = urlValue.value
                                    )
                                    val entryId = mainViewModel.saveEntry(newEntry)
                                    val savedEntry = mainViewModel.getEntryById(entryId.toInt())
                                    navigateToShowEntry(savedEntry)

                                } catch (e: Exception) {
                                    Log.e("AddEntryView", "Error: ${e.message}")
                                    snackbarHostState.showSnackbar("Error saving entry")
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(1f)
                ) {
                    Text("Save Entry")
                }
            }
        }
    }
}