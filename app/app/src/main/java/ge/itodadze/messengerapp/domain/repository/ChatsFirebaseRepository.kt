package ge.itodadze.messengerapp.domain.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import ge.itodadze.messengerapp.viewmodel.models.Chat

class ChatsFirebaseRepository(dbUrl: String):ChatsRepository {

    private var chats: DatabaseReference = Firebase
        .database(dbUrl)
        .getReference("chats")

    override fun getUsersChatsByNickname(nickname: String?, handler: CallbackHandler<Chat>) {
            // to do
    }


}