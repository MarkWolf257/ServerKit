package com.markwolf.serverkit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markwolf.serverkit.data.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun FolderPicker(initial: String, onDismiss: () -> Unit) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        var pickedFolder by remember { mutableStateOf(initial) }

        Card {
            Column (Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                Text(
                    text = pickedFolder,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                )
                HorizontalDivider()
                Column {
                    Text(
                        text = "..",
                        modifier = Modifier.clickable {
                            pickedFolder = File(pickedFolder).parent ?: pickedFolder
                        }.fillMaxWidth().padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    File(pickedFolder).listFiles()?.forEach { file ->
                        if (file.isDirectory) {
                            Text(
                                text = file.name,
                                modifier = Modifier.clickable {
                                    pickedFolder = file.absolutePath
                                }.fillMaxWidth().padding(8.dp),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }
                Button (
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            PreferencesManager.setRootPath(context, pickedFolder)
                            onDismiss()
                        }
                    }
                ) {
                    Text("Select Folder")
                }
            }
        }
    }
}