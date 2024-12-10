package com.markwolf.serverkit

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.markwolf.serverkit.data.PreferencesManager
import com.markwolf.serverkit.ui.components.BaseListItem
import com.markwolf.serverkit.ui.components.FolderPicker
import com.markwolf.serverkit.ui.theme.ServerKitTheme


class SettingActivity : ComponentActivity() {
    var permissionHandler: ((Boolean) -> Unit)? = null
    var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            permissionHandler?.invoke(isGranted)
        }

        setContent {
            ServerKitTheme {
                SettingScreen()
            }
        }
    }
}


@Composable
fun SettingScreen() {
    Scaffold (
        topBar = { SettingTopBar() }
    ) { innerPadding ->
        Column (Modifier.padding(innerPadding).padding(16.dp, 0.dp)) {
            RootFolderSetting()
        }
    }
}


@Composable
fun RootFolderSetting() {
    val activity = LocalContext.current as SettingActivity
    var showFolderPicker by remember { mutableStateOf(false) }

    val rootPath = PreferencesManager
        .getRootPath(activity)
        .collectAsState(initial = "")
        .value

    val truncatedPath = if (rootPath.length > 20) {
        "…${rootPath.takeLast(20)}"
    } else {
        rootPath
    }

    BaseListItem(
        iconId = R.drawable.ic_folder,
        heading = "Root Folder",
        subheading = truncatedPath,
        buttonIconId = R.drawable.ic_folder_outline,
    ) {
        activity.permissionHandler = { isGranted: Boolean ->
            showFolderPicker = isGranted
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissionLauncher?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            activity.permissionHandler?.invoke(true)
        }
    }
    if (showFolderPicker) {
        FolderPicker(if (rootPath == "") "/" else rootPath) {
            showFolderPicker = false
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopBar() {
    val activity = LocalContext.current as Activity

    Column {
        TopAppBar(
            title = { Text("Settings") },
            navigationIcon = {
                IconButton(onClick = { activity.finish() }) {
                    Icon (
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        )
        HorizontalDivider()
    }
}