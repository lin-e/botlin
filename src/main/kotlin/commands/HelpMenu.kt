package `in`.eugenel.botlin.commands

import `in`.eugenel.botlin.*

import discord4j.core.`object`.entity.Message

class HelpMenu : Command {
    override val description: String = "Displays this menu"
    override val triggers: List<String> = listOf("help")
    override val syntax: String = ""
    var bot: Botlin? = null

    override fun run(msg: Message, args: List<String>): Message {
        val resp = "Valid commands are:" + bot!!.commands.values.map { c ->
            val prefix = "\n    " + bot!!.trigger
            val syntax = c.syntax + (if (c.syntax == "") "" else " ") + " - "
            prefix + c.triggers.joinToString(", ") + syntax + c.description
        }.joinToString("")
        return send(msg.channel, resp)
    }

}