package it.fancypixel.boilerplate.components.events

import it.fancypixel.boilerplate.constants.MessageEventCode

class MessageEvent(messageCode: MessageEventCode) {

    var messageCode: MessageEventCode
        protected set

    init {
        this.messageCode = messageCode
    }
}
