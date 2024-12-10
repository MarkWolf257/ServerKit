package com.markwolf.serverkit.network

import androidx.annotation.DrawableRes
import fi.iki.elonen.NanoHTTPD
import java.io.File

open class BaseServer(port: Int, val rootFile: File, val label: String, val iconId: Int) : NanoHTTPD(port)
