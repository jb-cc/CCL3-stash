package com.cc221012_cc221016.stash.ui.views.composables

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.util.Log
import android.webkit.URLUtil
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cc221012_cc221016.stash.R
import com.cc221012_cc221016.stash.data.Entries
import com.cc221012_cc221016.stash.models.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(mainViewModel: MainViewModel, navigateToShowEntry: (Entries) -> Unit, onAddEntryClick: () -> Unit) {
    val colorScheme = colorScheme
    val mainViewState by mainViewModel.mainViewState.collectAsState()
    Log.d("HomeView", "Recomposing with entries: ${mainViewState.entries}")
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    MaterialTheme(colorScheme = colorScheme) {

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.Top // Align top part to the top
                    ) {
                        // Top part with title
                        Text(
                            text = "Stash",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        )
                        Divider()

                        // Spacer to push the "Log out" button to the bottom
                        Spacer(modifier = Modifier.weight(1f, fill = true))

                        // "Log Out" button at the bottom
                        NavigationDrawerItem(
                            label = { Text(text = "Log out") },
                            icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Log Out") },
                            selected = false,
                            onClick = { mainViewModel.logOut() },
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            },
            gesturesEnabled = true
        )
        {


            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    CenterAlignedTopAppBar(
                        title = {
                            Row(Modifier.fillMaxWidth()) {
                                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    Text("Your Stash")
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(top = 50.dp)
                ) {


                    // Ensure LazyColumn is not in an unbounded vertically expandable container
                    if (mainViewState.entries.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.passkey),
                                contentDescription = "Desired Icon",
                                modifier = Modifier.size(160.dp),
                                colorScheme.outline
                            )
                            Text(
                                "Nothing here yet...",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.align(Alignment.TopCenter).padding(top = 160.dp),
                                color = colorScheme.outline
                            )
                            Text(
                                "Add an entry to get started",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.align(Alignment.TopCenter).padding(top = 200.dp),
                                color = colorScheme.outline
                            )
                        }
                    } else {
                        LazyColumn {
                            items(mainViewState.entries) { entry ->
                                OutlinedCard(
                                    onClick = { navigateToShowEntry(entry) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        // Load and display the favicon or the initial letter
                                        EntryItem(entry)
                                        Spacer(modifier = Modifier.width(16.dp)) // Add spacing here

                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = entry.entryName,
                                                style = MaterialTheme.typography.titleMedium,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )
                                            Text(
                                                text = entry.entryUsername,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            // You can add more fields here as needed
                                        }
                                        IconButton(onClick = {
                                            val clip = ClipData.newPlainText(
                                                "password",
                                                entry.entryPassword
                                            )
                                            clipboardManager.setPrimaryClip(clip)
                                            Toast.makeText(context, "Password copied to clipboard", Toast.LENGTH_LONG).show()
                                        }) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(id = R.drawable.content_copy),
                                                contentDescription = "Copy Icon"
                                            )
                                        }
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(60.dp))
                            }
                        }
                    }

                }
                ExtendedFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = colorScheme.onPrimary,
                    onClick = {
                        onAddEntryClick()
                    },
                    icon = { Icon(Icons.Outlined.Edit, contentDescription = "Edit") },
                    text = { Text("Add entry") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            }
        }
    }


}

fun isValidUrl(url: String): Boolean {
    // Check if the URL already has a scheme
    if (!url.contains(".")) {
        return false
    }
    if (!url.startsWith("://") && !url.startsWith("https://")) {
        // Append a default scheme if it's missing
        return URLUtil.isValidUrl("http://$url")
    }
    // URL has a scheme, check it directly
    return URLUtil.isValidUrl(url)
}

@SuppressLint("RememberReturnType")
@Composable
fun getFaviconUrl(url: String): String {
    val processedUrl = if (url.startsWith("http://") || url.startsWith("https://")) {
        url // URL already contains a protocol
    } else {
        "http://$url" // Add "http://" if URL does not have a protocol
    }

    return "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=$processedUrl&size=64"
}
@Composable
fun EntryInitial(entryName: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .background(colorScheme.primary, shape = CircleShape)
            .padding(end = 8.dp)
    ) {
        Text(
            text = "   " + entryName.take(1).uppercase() + " ",
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.onPrimary
        )
    }
}

@Composable
fun EntryItem(entry: Entries) {
    val imageUrl = getFaviconUrl(entry.entryUrl)
    val loadError = remember { mutableStateOf(false) }

    if (!loadError.value && isValidUrl(entry.entryUrl)) {
        Image(
            painter = rememberAsyncImagePainter(
                model = imageUrl,
                onError = { loadError.value = true }
            ),
            contentDescription = "Favicon",
            modifier = Modifier.size(40.dp)
        )
    } else {
        EntryInitial(entry.entryName)
    }
}