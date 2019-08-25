package `in`.eugenel.botlin

import `in`.eugenel.botlin.commands.*

fun main() {
    val bot = Botlin("config.json")
    bot.add(Ping())
    bot.start()
}