package com.markwolf.serverkit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import com.markwolf.serverkit.data.PreferencesManager
import com.markwolf.serverkit.network.MusicServer
import com.markwolf.serverkit.ui.components.ServerListItem
import com.markwolf.serverkit.ui.theme.ServerKitTheme
import com.markwolf.serverkit.utils.getLocalIPv4

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.io.File


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize rootPath with app's private directory
        CoroutineScope(Dispatchers.IO).launch {
            PreferencesManager.setRootPath(
                this@MainActivity,
                this@MainActivity.getExternalFilesDir(null)?.absolutePath ?: ""
            )
        }

        setContent {
            ServerKitTheme {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    Scaffold(
        topBar = { MainTopBar() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding).padding(16.dp, 0.dp)) {
            ServerList()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar() {
    val context = LocalContext.current
    Column {
        TopAppBar(
            title = { Text(getLocalIPv4() ?: "127.0.0.1") },
            actions = {
                IconButton(
                    onClick = {
                        val intent = Intent(context, SettingActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = "Settings",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        )
        HorizontalDivider()
    }
}


@Composable
fun ServerList() {
    val context = LocalContext.current
    val rootPath = PreferencesManager
        .getRootPath(context)
        .collectAsState(initial = "")

    if (rootPath.value != "") {
        val musicDir = File(rootPath.value, "music")
        if (!musicDir.exists()) {
            musicDir.mkdirs()
        }
        ServerListItem(MusicServer(8080, musicDir))
    }
}
