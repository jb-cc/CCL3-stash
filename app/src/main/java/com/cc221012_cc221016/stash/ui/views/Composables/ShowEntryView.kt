package com.cc221012_cc221016.stash.ui.views.Composables

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cc221012_cc221016.stash.R
import com.cc221012_cc221016.stash.data.Entries
import com.cc221012_cc221016.stash.models.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
suspend fun ShowEntryView(
    entryId: Int,
    onBack: () -> Unit,
    mainViewModel: MainViewModel,
    onEditEntry: (Entries) -> Unit,
    onDeleteEntry: (Entries) -> Unit
)  {
    val passwordVisibility = remember { mutableStateOf(false) }
    //val entryName = remember { mutableStateOf("Instagram") }
    //val url = remember { mutableStateOf("https://www.instagram.com/") }
    //val email = remember { mutableStateOf("yourname@email.com") }
    //val password = remember { mutableStateOf("password123") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val menuExpanded = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val entry by mainViewModel.getEntryById(entryId).collectAsState(initial = null)



    if(entry != null) {
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
                    title = { Text(entry!!.entryName) },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { menuExpanded.value = true }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.three_dot_options),
                                contentDescription = "Options Icon"
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded.value,
                            onDismissRequest = { menuExpanded.value = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    menuExpanded.value = false
                                    onEditEntry(entry!!) // Navigate to the EditEntryView
                                },
                                text = { Text("Edit Entry") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = "Edit Icon"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                onClick = { showDialog.value = true },
                                text = { Text("Delete Entry") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Delete Icon"
                                    )
                                }
                            )
                        }

                        if (showDialog.value) {
                            AlertDialog(
                                onDismissRequest = { showDialog.value = false },
                                title = { Text("Delete Credentials?") },
                                text = { Text("This action cannot be undone.") },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            onDeleteEntry(entry!!) // Delete the entry
                                            showDialog.value = false
                                            menuExpanded.value = false
                                            onBack() // Navigate back after deletion
                                        }
                                    ) {
                                        Text("Delete", color = Color(0xFFDC362E))
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            showDialog.value = false
                                            menuExpanded.value = false
                                        }
                                    ) {
                                        Text("Cancel", color = colorScheme.primary)
                                    }
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))



                ListItem(
                    headlineText = { Text("URL") },
                    supportingText = { Text(entry!!.entryUrl) },
                    leadingContent = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.link),
                            contentDescription = "URL Icon"
                        )
                    },
                )

                Divider()

                ListItem(
                    headlineText = { Text("Email") },
                    supportingText = { Text(entry!!.entryUsername) },
                    leadingContent = {
                        Icon(
                            Icons.Outlined.Email,
                            contentDescription = "Email Icon"
                        )
                    },
                )

                Divider()

                ListItem(
                    headlineText = { Text("Password") },
                    supportingText = { Text(if (passwordVisibility.value) entry!!.entryPassword else "••••••••") },
                    leadingContent = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.key),
                            contentDescription = "Key Icon"
                        )
                    },
                    trailingContent = {
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

                Divider()
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                SnackbarHost(
                    hostState = snackbarHostState
                )
                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("password", entry!!.entryPassword)
                            clipboard.setPrimaryClip(clip)

                            snackbarHostState.showSnackbar("Password copied")
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Text("Copy Password")
                }
            }
        }} else {
        // Show progress indicator while loading
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
