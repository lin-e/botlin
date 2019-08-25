package `in`.eugenel.botlin.commands

import `in`.eugenel.botlin.*
import `in`.eugenel.botlin.instagram.*

import discord4j.core.`object`.entity.Message

import java.net.HttpURLConnection
import java.net.URL

class InstagramMonitor : Command {
    override val description: String = "Monitors an Instagram account for new posts"
    override val triggers: List<String> = listOf("igmonitor")
    override val syntax: String = "<username> <timeout in seconds>"
    var bot: Botlin? = null

    var active: MutableList<Monitor> = mutableListOf()

    override fun run(msg: Message, args: List<String>): Message {
        return when (args.size) {
            0 -> {
                when (active.size) {
                    0 -> send(msg.channel, "There are no active monitors")
                    else -> {
                        val cnt = if (active.size == 1) "is 1" else "are ${active.size}"
                        val mnt = if (active.size == 1) "monitor" else "monitors"
                        val header = "There $cnt active $mnt:\n    "
                        val resp = header + active.map {
                            val here = it.channel.equals(msg.channel.block()!!.id.asLong())
                            val channel = "${it.channel}${if (here) " (here)" else ""}"
                            "${it.username} | Timeout: ${it.timeout}s | Channel: $channel"
                        }.joinToString("\n    ")
                        send(msg.channel, resp)
                    }
                }
            }
            2 -> {
                val t = args[1].toIntOrNull()
                when {
                    t == null -> invalidSyntax(msg.channel)
                    t < 1 -> invalidSyntax(msg.channel)
                    else -> {
                        val user = args[0].toLowerCase()
                        val valid = try {
                            val page = URL("https://www.instagram.com/$user/")
                            with(page.openConnection() as HttpURLConnection) {
                                requestMethod = "GET"
                                inputStream.bufferedReader().use {
                                    val raw = it.readText()
                                }
                            }
                            true
                        } catch (e: Exception) {
                            println(e.toString())
                            false
                        }
                        if (valid) {
                            val m = Monitor(user, t, msg.channel.block()!!.id.asLong())
                            m.bot = bot
                            active.add(m)
                            send(msg.channel, "Starting to monitor '$user' for new posts")
                        } else send(msg.channel, "User not found!")
                    }
                }
            }
            else -> invalidSyntax(msg.channel)
        }
    }
}