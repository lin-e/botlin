package `in`.eugenel.botlin

import `in`.eugenel.botlin.commands.*

fun main() {
    val bot = Botlin("config.json")

    bot.add(Ping())

    val menu = HelpMenu()
    menu.bot = bot
    bot.add(menu)

    bot.start()
}