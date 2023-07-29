package ge.itodadze.messengerapp.viewmodel.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Chat(val messages: MutableList<Message>? = null,
                val identifier: String? = null) {

    fun withIdAndMessages(identifier: String): Chat {
        if(messages == null){
            return Chat(mutableListOf(), identifier)
        }
        return Chat(messages, identifier)
    }
}