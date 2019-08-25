package `in`.eugenel.botlin

import `in`.eugenel.botlin.commands.*
import `in`.eugenel.botlin.Logger.*
import `in`.eugenel.botlin.instagram.*

import java.lang.Exception
import kotlin.concurrent.timer

fun main() {
    val bot = Botlin("config.json")

    bot.add(Ping())
    bot.add(MessageInfo())

    val igMonitor = InstagramMonitor()
    val igMonitors = mutableListOf<Monitor>()
    igMonitor.active = igMonitors
    igMonitor.bot = bot
    bot.add(igMonitor)

    timer("", false, 0, 1000) {
        igMonitors.forEach {
            try {
                it.tick()
            } catch (e: Exception) {
                bot.logger.log(LogType.ERR, "InstagramMonitor -> ${it.username}: $e")
            }
        }
    }

    val menu = HelpMenu()
    menu.bot = bot
    bot.add(menu)

    bot.start()
}