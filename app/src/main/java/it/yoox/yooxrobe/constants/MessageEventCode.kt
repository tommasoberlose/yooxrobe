package it.yoox.yooxrobe.constants

enum class MessageEventCode private constructor(val code: Int) {

    LOGGED_IN(0),
    LOGGED_OUT(1),
    SERVER_ERROR(2)
}
