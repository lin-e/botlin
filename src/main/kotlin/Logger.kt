package `in`.eugenel

class Logger {
    enum class LogType {
        BOT,
        MESSAGE,
        ERROR
    }

    fun log(type: LogType, message: String) {
        println("[$type] $message")
    }
}