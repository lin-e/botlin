package `in`.eugenel.botlin.commands

import discord4j.core.`object`.entity.Message

class InstagramMonitor : Command {
    override val description: String = "Displays this menu"
    override val triggers: List<String> = listOf("help")
    override val syntax: String = ""

    override fun run(msg: Message, args: List<String>): Message {
        return send(msg.channel, "TODO")
    }

}