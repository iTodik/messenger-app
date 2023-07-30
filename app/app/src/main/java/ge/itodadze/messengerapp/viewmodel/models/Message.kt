package ge.itodadze.messengerapp.viewmodel.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*


@IgnoreExtraProperties
data class Message(
    val identifier: String? = null,
    val text: String? = null, val sender_id: String? = null,
    val receiver_id: String? = null, val timestamp: Date? = null
){

    fun withId(identifier: String): Message {
        return Message(identifier, text, sender_id, receiver_id, timestamp)
    }

}