package com.cc221012_cc221016.stash.ui.views.composables

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import com.cc221012_cc221016.stash.R
import com.cc221012_cc221016.stash.data.Entries
import com.cc221012_cc221016.stash.models.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowEntryView(
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
    val menuExpanded = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val entryState = remember { mutableStateOf<Entries?>(null) }
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val openInBrowserLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    LaunchedEffect(entryId) {
        entryState.value = mainViewModel.getEntryById(entryId)
    }

    val entry = entryState.value
    BackHandler {
        onBack()  // Define what should happen when back is pressed
    }



    MaterialTheme(colorScheme = colorScheme){
        if (entry != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { focusManager.clearFocus() })
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {

                    TopAppBar(
                        title = { Text(entry.entryName) },
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
                                        onEditEntry(entry) // Navigate to the EditEntryView
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
                                                onDeleteEntry(entry) // Delete the entry
                                                showDialog.value = false
                                                menuExpanded.value = false
                                                onBack() // Navigate back after deletion
                                            }
                                        ) {
                                            Text("Delete", color = colorScheme.error)
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



                    ListItem(
                        headlineText = { Text("URL") },
                        supportingText = { Text(entry.entryUrl) },
                        leadingContent = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.link),
                                contentDescription = "URL Icon"
                            )
                        },
                        trailingContent = {
                            Row {
                                IconButton(onClick = {
                                    val formattedUrl = formatUrl(entry.entryUrl)
                                    if (formattedUrl != null){
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedUrl))
                                        openInBrowserLauncher.launch(intent)
                                    } else {
                                        Toast.makeText(context, "Invalid URL.", Toast.LENGTH_LONG).show()
                                    }
                                }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.open_in_new),
                                        contentDescription = "Open in Browser Icon"
                                    )
                                }
                                IconButton(onClick = {
                                    val clip = ClipData.newPlainText("URL", entry.entryUrl)
                                    clipboardManager.setPrimaryClip(clip)
                                    Toast.makeText(context, "URL copied", Toast.LENGTH_LONG).show()
                                }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.content_copy),
                                        contentDescription = "Copy Icon"
                                    )
                                }
                            }
                        }
                    )

                    Divider()

                    ListItem(
                        headlineText = { Text("Email / Username") },
                        supportingText = { Text(entry.entryUsername) },
                        leadingContent = {
                            Icon(
                                Icons.Outlined.Email,
                                contentDescription = "Email Icon"
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = {
                                val clip = ClipData.newPlainText("Email / Username", entry.entryUsername)
                                clipboardManager.setPrimaryClip(clip)
                                Toast.makeText(context, "Email / Username copied", Toast.LENGTH_LONG).show()
                            }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.content_copy),
                                    contentDescription = "Copy Icon"
                                )
                            }
                        }
                    )

                    Divider()

                    ListItem(
                        headlineText = { Text("Password") },
                        supportingText = { Text(if (passwordVisibility.value) entry.entryPassword else "••••••••") },
                        leadingContent = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.key),
                                contentDescription = "Key Icon"
                            )
                        },
                        trailingContent = {
                            Row {
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
                                IconButton(onClick = {
                                    val clip = ClipData.newPlainText("Password", entry.entryPassword)
                                    clipboardManager.setPrimaryClip(clip)
                                    Toast.makeText(context, "Password copied", Toast.LENGTH_LONG).show()
                                }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.content_copy),
                                        contentDescription = "Copy Icon"
                                    )
                                }
                            }
                        }
                    )

                    Divider()
                }
            }
        } else {
            // Show progress indicator while loading
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

fun formatUrl(url: String): String? {
    val urlRegex = "^(http://www.|https://www.|http://|https://)?[a-z0-9]+([-.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$".toRegex()
    return if (urlRegex.matches(url)) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "http://$url"
        } else {
            url
        }
    } else {
        null
    }
}
