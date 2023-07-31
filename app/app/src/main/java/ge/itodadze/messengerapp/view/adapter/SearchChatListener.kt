package ge.itodadze.messengerapp.view.adapter

import ge.itodadze.messengerapp.view.model.ViewChat

interface SearchChatListener {
    fun userClicked(user: ViewChat)
}