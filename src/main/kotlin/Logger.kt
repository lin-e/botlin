package `in`.eugenel.botlin

class Logger {
    enum class LogType {
        BOT,
        MSG,
        ERR
    }

    fun log(type: LogType, message: String) {
        println("[$type] $message")
    }
}