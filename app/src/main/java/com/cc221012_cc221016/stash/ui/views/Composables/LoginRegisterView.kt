package com.cc221012_cc221016.stash.ui.views.Composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRegisterView(hasUser: Boolean) {
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var masterPassword by remember { mutableStateOf(TextFieldValue("")) }
    var repeatMasterPassword by remember { mutableStateOf(TextFieldValue("")) }

    Column {
        Text(
            text = "Stash",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if (hasUser) {
            OutlinedTextField(
                value = password,
                onValueChange = { newPassword -> password = newPassword },
                label = { Text(text = "Master Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        } else {
            OutlinedTextField(
                value = masterPassword,
                onValueChange = { newMasterPassword -> masterPassword = newMasterPassword },
                label = { Text(text = "MasterPassword") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            OutlinedTextField(
                value = repeatMasterPassword,
                onValueChange = { newRepeatMasterPassword -> repeatMasterPassword = newRepeatMasterPassword },
                label = { Text(text = "Repeat Master Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
    }
}