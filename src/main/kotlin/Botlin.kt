package `in`.eugenel.botlin

import `in`.eugenel.botlin.Logger.*
import `in`.eugenel.botlin.commands.*

import com.beust.klaxon.*

import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.entity.Message
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent

class Botlin {
    val client: DiscordClient
    val logger: Logger
    val trigger: String
    val commands: MutableMap<String, Command>

    constructor (config: String) {
        val json = Parser().parse(config) as JsonObject
        client = DiscordClientBuilder(json["token"].toString()).build()
        logger = Logger()
        trigger = json["trigger"].toString()
        commands = mutableMapOf()
    }

    fun start() {
        val dispatcher = client.eventDispatcher
        dispatcher.on(ReadyEvent::class.java).subscribe {
            logger.log(LogType.BOT, "Logged in as ${it.self.username}")
        }
        dispatcher.on(MessageCreateEvent::class.java).subscribe {
            MessageThread(it.message).start()
        }
        client.login().block()
    }

    fun add(c: Command) {
        c.triggers.forEach {
            when (it) {
                in commands.keys -> logger.log(LogType.BOT, "Conflict: $it")
                else -> commands[it] = c
            }
        }
    }

    inner class MessageThread : Thread {
        val msg: Message

        constructor (m: Message) {
            msg = m
        }

        override fun run() {
            val author = msg.author.get()
            val content = msg.content.get()

            if (author.id.asLong() == client.selfId.get().asLong()) return
            if (!content.startsWith(trigger)) return

            logger.log(LogType.MSG, "${author.username}: $content")
            val parts = content.split(" ")
            val command = parts[0].removePrefix(trigger).toLowerCase()
            val args = parts.drop(1)

            (commands[command] ?: UnknownCommand()).run(msg, args)
        }
    }
}