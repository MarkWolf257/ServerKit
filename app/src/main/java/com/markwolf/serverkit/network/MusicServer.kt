package com.markwolf.serverkit.network

import android.media.MediaMetadataRetriever
import com.markwolf.serverkit.R
import com.markwolf.serverkit.model.MusicData
import com.markwolf.serverkit.model.MusicFolder
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class MusicServer(port: Int, rootFile: File) : BaseServer(port, rootFile, "Music Server", R.drawable.ic_music_server) {
    private val musicFolders = mutableListOf<MusicFolder>()

    private fun getMusicFolders() {
        val retriever = MediaMetadataRetriever()
        musicFolders.clear()

        val stack = ArrayDeque<File>()
        stack.add(rootFile)

        while (stack.isNotEmpty()) {
            val folder = stack.removeLast()
            val files = mutableListOf<MusicData>()
            val children = folder.listFiles()

            children?.forEach { child ->
                if (child.isDirectory) {
                    stack.add(child)
                } else {
                    if (child.extension == "mp3") {
                        retriever.setDataSource(child.absolutePath)
                        val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        files.add(MusicData(title ?: child.name, child.name))
                    }
                }
            }

            musicFolders.add(MusicFolder(folder.name, folder.absolutePath, files.toTypedArray()))
        }

        retriever.release()
    }

    override fun start() {
        getMusicFolders()
        super.start()
    }

    override fun serve(session: IHTTPSession): Response {
        val method = session.method.toString().uppercase()
        val uri = session.uri

        when (method) {
            "GET" -> {
                when (uri) {
                    "/music-dirs" -> {
                        val str = try {
                            Json.encodeToString(musicFolders)
                        } catch (e: Exception) {
                            e.message ?: "Unknown Error"
                        }

                        return newFixedLengthResponse(Response.Status.OK, "text/plain", str)
                    }
                }
            }
        }

        return newFixedLengthResponse(rootFile.absolutePath ?: "Couldn't open music folder")
    }
}