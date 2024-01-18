package com.cc221012_cc221016.stash.ui.views.Composables

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.util.Log
import android.webkit.URLUtil
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
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme(colorScheme = colorScheme) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                TopAppBar(
                    title = {
                        Row(Modifier.fillMaxWidth()) {
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text("Your Stash")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(modifier = Modifier
                .padding(16.dp)
                .padding(top = 50.dp)) {


                // Ensure LazyColumn is not in an unbounded vertically expandable container
                if (mainViewState.entries.isEmpty()) {
                    Text("No entries yet", style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn {
                        items(mainViewState.entries) { entry ->
                            OutlinedCard(
                                onClick = { navigateToShowEntry(entry)  },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    // Load and display the favicon or the initial letter
                                    if (isValidUrl(entry.entryUrl)) {
                                        val faviconUrl = getFaviconUrl(entry.entryUrl )
                                        Log.d("HomeView", "Loading favicon for URL: $faviconUrl")
                                        Image(
                                            painter = rememberAsyncImagePainter(faviconUrl),
                                            contentDescription = "Favicon",
                                            modifier = Modifier.size(40.dp)
                                        )
                                    } else {
                                        Log.d("HomeView", "Invalid URL, showing initial: ${entry.entryName}")
                                        EntryInitial(entryName = entry.entryName)
                                    }
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
                                        val clip = ClipData.newPlainText("password", entry.entryPassword)
                                        clipboardManager.setPrimaryClip(clip)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Password copied to clipboard")
                                        }
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
                contentColor = contentColorFor(MaterialTheme.colorScheme.primary),
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
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
    Log.d("HomeView", "getFaviconUrl: $url")
    val website = "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://$url&size=64"
    return website
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
            text = "  " + entryName.take(1).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}
