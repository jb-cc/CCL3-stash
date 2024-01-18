package com.cc221012_cc221016.stash.ui.views.composables


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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.cc221012_cc221016.stash.R
import com.cc221012_cc221016.stash.data.Entries
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryView(entry: Entries, onBack: () -> Unit,  onSave: (Entries) -> Unit) {


    val passwordVisibility = remember { mutableStateOf(false) }

    // Initialize state with actual entry data
    val nameValue = remember { mutableStateOf(entry.entryName) }
    val urlValue = remember { mutableStateOf(entry.entryUrl) }
    val emailValue = remember { mutableStateOf(entry.entryUsername) }
    val passwordValue = remember { mutableStateOf(entry.entryPassword) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isNameEmpty by remember { mutableStateOf(false) }
    var isEmailEmpty by remember { mutableStateOf(false) }
    var isPasswordEmpty by remember { mutableStateOf(false) }



    BackHandler {
        onBack()  // Go back to previous screen
    }


    MaterialTheme(colorScheme = colorScheme){
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                TopAppBar(
                    title = { Text("Edit Entry") },
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
                    onValueChange = { newValue ->
                        nameValue.value = newValue
                        isNameEmpty = newValue.isEmpty()
                    },
                    supportingText = { if (isNameEmpty) Text("Required") },
                    label = { Text("Name") },
                    isError = nameValue.value.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.star),
                            contentDescription = "Star Icon"
                        )
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = urlValue.value,
                    onValueChange = { urlValue.value = it },
                    label = { Text("URL") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.link),
                            contentDescription = "URL Icon"
                        )
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = emailValue.value,
                    onValueChange = { newValue ->
                        emailValue.value = newValue
                        isEmailEmpty = newValue.isEmpty()
                    },
                    supportingText = { if (isEmailEmpty) Text("Required") },
                    label = { Text("Email") },
                    isError = emailValue.value.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Email,
                            contentDescription = "Email Icon"
                        )
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = passwordValue.value,
                    onValueChange = { newValue ->
                        passwordValue.value = newValue
                        isPasswordEmpty = newValue.isEmpty()
                    },
                    supportingText = { if (isPasswordEmpty) Text("Required") },
                    label = { Text("Password") },
                    isError = passwordValue.value.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
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
                    }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            )
            {
                Spacer(Modifier.weight(1f))

                SnackbarHost(
                    hostState = snackbarHostState,
                )

                Button(
                    onClick = {
                        // Check if any of the fields are empty
                        if (nameValue.value.isEmpty() || emailValue.value.isEmpty() || passwordValue.value.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please fill in all mandatory fields")
                            }
                            return@Button   // Exit the onClick function
                        }
                        // Construct updated entry from user input
                        val updatedEntry = Entries(
                            entryName = nameValue.value,
                            entryUsername = emailValue.value,
                            entryPassword = passwordValue.value,
                            entryUrl = urlValue.value,
                            entryID = entry.entryID // Preserve the original ID
                        )
                        onSave(updatedEntry) // Save the updated entry
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Update Entry")
                }
            }
        }
    }
}