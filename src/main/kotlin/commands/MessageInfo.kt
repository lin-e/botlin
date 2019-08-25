package `in`.eugenel.botlin.commands

import discord4j.core.`object`.entity.Message

class MessageInfo : Command {
    override val description: String = "Displays information about the current message"
    override val triggers: List<String> = listOf("minfo")
    override val syntax: String = ""

    override fun run(msg: Message, args: List<String>): Message {
        return send(
            msg.channel, listOf(
                "Channel ID: ${msg.channel.block()!!.id.asLong()}",
                "Channel Name: ${msg.channel.block()!!.mention}",
                "Sender ID: ${msg.author.get().id.asLong()}",
                "Sender Name: ${msg.author.get().mention}"
            ).joinToString("\n")
        )
    }

}