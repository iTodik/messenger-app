package ge.itodadze.messengerapp.view.adapter

import ge.itodadze.messengerapp.view.model.ViewUser

interface SearchListener {
    fun userClicked(user: ViewUser)
}