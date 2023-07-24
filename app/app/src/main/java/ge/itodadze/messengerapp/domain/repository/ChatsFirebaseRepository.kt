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

    override fun getUsersLastChats(user_id: String?, handler: CallbackHandler<List<Chat>>?) {
            if(user_id == null){
                handler?.onResultEmpty("id not provided")
                return
            }

            val chatList: MutableList<Chat> = arrayListOf()

            chats.orderByChild("sender_id").equalTo(user_id).get().addOnSuccessListener {
                val chats: MutableList<Chat> = arrayListOf()
                for (snapshot in it.children){
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        chats.add(chat)
                    }
                }

                chatList.addAll(chats)


            }.addOnFailureListener{
                // handler?.onResultEmpty("has no sent messages")
            }

            chats.orderByChild("receiver_id").equalTo(user_id).get().addOnSuccessListener {
                val chats: MutableList<Chat> = arrayListOf()
                for (snapshot in it.children){
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        chats.add(chat)
                    }
                }
                chatList.addAll(chats)


            }.addOnFailureListener{
                // handler?.onResultEmpty("has no received messages")
            }

            if(chatList.size==0){
                handler?.onResultEmpty("no active chats found")
            } else{
                handler?.onResult(chatList)
            }


    }


}