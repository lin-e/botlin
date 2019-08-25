package `in`.eugenel.botlin.commands

import discord4j.core.`object`.entity.Message

class Ping : Command {
    override val description: String = "Just a ping testing command"
    override val triggers: List<String> = listOf("ping")
    override val syntax: String = ""

    override fun run(msg: Message, args: List<String>): Message {
        val m = msg.channel.block().createMessage("Pong!").block()
        return m!!
    }

}