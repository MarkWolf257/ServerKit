package com.markwolf.serverkit.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.markwolf.serverkit.R
import com.markwolf.serverkit.network.BaseServer

@Composable
fun ServerListItem(server: BaseServer) {
    val isAlive = remember { mutableStateOf(server.isAlive) }
    val port = remember { mutableStateOf(server.listeningPort) }

    BaseListItem(
        iconId = server.iconId,
        heading = server.label,
        subheading = "Port: ${port.value}",
        buttonIconId = if (isAlive.value) {
            R.drawable.ic_stop_server
        } else {
            R.drawable.ic_start_server
        }
    ) {
        if (server.isAlive) {
            server.stop()
            port.value = -1
            isAlive.value = false
        } else {
            server.start()
            port.value = server.listeningPort
            isAlive.value = true
        }
    }
}