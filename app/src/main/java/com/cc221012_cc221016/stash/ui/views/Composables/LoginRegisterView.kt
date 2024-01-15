package com.cc221012_cc221016.stash.ui.views.Composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRegisterView(hasUser: Boolean) {
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var masterPassword by remember { mutableStateOf(TextFieldValue("")) }
    var repeatMasterPassword by remember { mutableStateOf(TextFieldValue("")) }

    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Stash",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 10.dp)
            )

            Column(modifier = Modifier.padding(top = 200.dp)) {
                if (hasUser) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { newPassword -> password = newPassword },
                        label = { Text(text = "Master Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    OutlinedTextField(
                        value = masterPassword,
                        onValueChange = { newMasterPassword -> masterPassword = newMasterPassword },
                        label = { Text(text = "MasterPassword") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    OutlinedTextField(
                        value = repeatMasterPassword,
                        onValueChange = { newRepeatMasterPassword -> repeatMasterPassword = newRepeatMasterPassword },
                        label = { Text(text = "Repeat Master Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}