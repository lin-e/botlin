package `in`.eugenel

import `in`.eugenel.Logger.LogType
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.entity.Message
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent

class Botlin {
    val client: DiscordClient
    val logger: Logger
    constructor (config: String) {
        client = DiscordClientBuilder(token).build()
        logger = Logger()
    }
    fun start() {
        val dispatcher = client.eventDispatcher
        dispatcher.on(ReadyEvent::class.java).subscribe {
            logger.log(LogType.BOT, "Logged in as ${it.self.username}")
        }
        dispatcher.on(MessageCreateEvent::class.java).subscribe {
            MessageThread(it.message).start()
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
            // TODO: TRIGGER SKIP
            logger.log(LogType.MESSAGE, "${author.username}: $content")
            val parts = content.split(" ")

        }
    }
}