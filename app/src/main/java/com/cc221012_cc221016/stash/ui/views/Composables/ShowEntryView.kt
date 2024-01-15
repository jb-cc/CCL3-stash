package com.cc221012_cc221016.stash.ui.views.Composables

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.cc221012_cc221016.stash.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowEntryView() {
    val passwordVisibility = remember { mutableStateOf(false) }
    val entryName = remember { mutableStateOf("Instagram") }
    val url = remember { mutableStateOf("https://www.instagram.com/") }
    val email = remember { mutableStateOf("yourname@email.com") }
    val password = remember { mutableStateOf("password123") }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text(entryName.value) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back button click */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle button click */ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.three_dot_options),
                            contentDescription = "Options Icon"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = entryName.value,
                onValueChange = { entryName.value = it },
                label = { Text("Name") },
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
                value = url.value,
                onValueChange = { url.value = it },
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
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
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
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.key),
                        contentDescription = "Key Icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) ImageVector.vectorResource(id = R.drawable.visibility_on) else ImageVector.vectorResource(id = R.drawable.visibility_off),
                            contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                        )
                    }
                }
            )
        }

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.9f)
        ) {
            Text("Update Entry")
        }
    }
}