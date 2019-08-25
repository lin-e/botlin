package `in`.eugenel.botlin.instagram

import `in`.eugenel.botlin.Botlin

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser

import java.net.HttpURLConnection
import java.net.URL

class Monitor {
    val username: String
    val timeout: Int
    val channel: Long

    var bot: Botlin? = null

    var timeSinceTick: Int = 0
    var lastTimestamp: Int = 0

    constructor (usr: String, time: Int, chn: Long) {
        username = usr
        timeout = time
        channel = chn
        reset()
    }

    fun reset() {
        timeSinceTick = 0
    }

    fun notify(msg: String) {
        bot!!.send(channel, msg)
    }

    fun tick() {
        timeSinceTick++
        if (timeSinceTick >= timeout) {
            val page = URL("https://www.instagram.com/$username/")
            with(page.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                inputStream.bufferedReader().use {
                    val raw = it.readText()
                    val p = "window._sharedData = "
                    val s = ";</script>"
                    val pattern = Regex("window._sharedData = (.*?);</script>")
                    val range = pattern.find(raw)?.range ?: (0..0)
                    val data = raw.substring(range).removePrefix(p).removeSuffix(s)
                    val decoded = Parser.default().parse(StringBuilder(data)) as JsonObject
                    val node = decoded.obj("entry_data")
                        ?.array<JsonObject>("ProfilePage")?.get(0)
                        ?.obj("graphql")
                        ?.obj("user")
                        ?.obj("edge_owner_to_timeline_media")
                        ?.array<JsonObject>("edges")?.get(0)
                        ?.obj("node")
                    val ts = node?.int("taken_at_timestamp") ?: -1
                    if (ts > lastTimestamp) {
                        lastTimestamp = ts
                        //val img = node?.string("thumbnail_src") ?: ""
                        val likes = node?.obj("edge_liked_by")?.int("count") ?: 0
                        val comments = node?.obj("edge_media_to_comment")?.int("count") ?: 0
                        val url = "https://www.instagram.com/p/${node?.string("shortcode") ?: ""}"
                        val caption = node?.obj("edge_media_to_caption")
                            ?.array<JsonObject>("edges")?.get(0)
                            ?.obj("node")
                            ?.string("text") ?: "[NO CAPTION]"
                        val msg = listOf(
                            "@here InstagramMonitor: $username has posted!",
                            caption,
                            "Currently has $likes like(s), and $comments comment(s)",
                            "Available at $url"
                        ).joinToString("\n\n")
                        notify(msg)
                    }
                }
            }
            reset()
        }
    }
}