import discord4j.core.*
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.*
import java.io.File

fun main() {
    val client = DiscordClientBuilder(File("config.json").readText()).build()
    client.eventDispatcher.on(ReadyEvent::class.java).subscribe {
        println(it.self.username)
    }
    client.eventDispatcher.on(MessageCreateEvent::class.java).subscribe {
        println(it.message.content)
    }
    client.login().block()
}