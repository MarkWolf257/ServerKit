package com.markwolf.serverkit.model

import kotlinx.serialization.Serializable

@Serializable
data class MusicFolder(val name: String, val path: String, val content: Array<MusicData>)
