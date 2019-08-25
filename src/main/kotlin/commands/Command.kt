package `in`.eugenel.botlin.commands

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.MessageChannel

import reactor.core.publisher.Mono

interface Command {
    val description: String
    val triggers: List<String>
    val syntax: String
    fun run(msg: Message, args: List<String>): Message

    fun send(c: Mono<MessageChannel>, body: String): Message {
        return c.block()!!.createMessage(body).block()!!
    }

    fun invalidSyntax(c: Mono<MessageChannel>): Message {
        return send(c, "Invalid syntax. The correct syntax for this command is '${triggers[0]} $syntax'.")
    }
}