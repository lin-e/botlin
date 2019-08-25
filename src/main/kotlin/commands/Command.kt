package `in`.eugenel.botlin.commands

import discord4j.core.`object`.entity.Message

interface Command {
    val description: String
    val triggers: List<String>
    val syntax: String
    fun run(msg: Message, args: List<String>): Message
}