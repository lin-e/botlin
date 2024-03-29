package `in`.eugenel.botlin.commands

import discord4j.core.`object`.entity.Message

class UnknownCommand : Command {
    override val description: String = ""
    override val triggers: List<String> = listOf()
    override val syntax: String = ""

    override fun run(msg: Message, args: List<String>): Message {
        return send(msg.channel, "Unknown command!")
    }

}