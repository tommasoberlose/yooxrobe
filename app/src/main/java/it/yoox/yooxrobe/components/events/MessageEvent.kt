package it.yoox.yooxrobe.components.events

import it.yoox.yooxrobe.constants.MessageEventCode

class MessageEvent(messageCode: MessageEventCode) {

    var messageCode: MessageEventCode
        protected set

    init {
        this.messageCode = messageCode
    }
}
