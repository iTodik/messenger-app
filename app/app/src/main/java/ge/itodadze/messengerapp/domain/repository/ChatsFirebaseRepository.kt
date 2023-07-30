package ge.itodadze.messengerapp.domain.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.itodadze.messengerapp.view.model.ViewChat
import ge.itodadze.messengerapp.view.model.ViewUser
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import ge.itodadze.messengerapp.viewmodel.models.Chat
import ge.itodadze.messengerapp.viewmodel.models.Message
import ge.itodadze.messengerapp.viewmodel.models.UserChats
import java.util.*

class ChatsFirebaseRepository(dbUrl: String):ChatsRepository {

    private var chats: DatabaseReference = Firebase
        .database(dbUrl)
        .getReference("chats")

    private var usersChats: DatabaseReference = Firebase
        .database(dbUrl)
        .getReference("users_chats")


    override fun getUsersChats(user_id: String?, handler: CallbackHandler<MutableList<ViewChat>>?) {

        if(user_id==null){
            handler?.onResultEmpty("User id not provided")
            return
        }

        usersChats.child(user_id).get().addOnSuccessListener { snapshot ->
            val chatList = snapshot.getValue(UserChats::class.java)

            val activeChats: MutableList<ViewChat>? = mutableListOf()
            if (chatList != null) {
                for(item in chatList.chat_ids!!){
                    chats.child(item).get().addOnSuccessListener {
                        it.getValue(Chat::class.java)?.let { it1 ->
                            activeChats?.add(ViewChat(chat_id = it1.identifier, ViewUser(nickname = null, profession = null, imgUri = null),
                                it1.messages?.get(0)
                            ))
                        }
                    }.addOnFailureListener {
                        handler?.onResultEmpty("active chat with given id not found")
                    }
                }
            }
            handler?.onResult(activeChats)

        }.addOnFailureListener{
            handler?.onResultEmpty("User has no active chats")
        }

    }

    override fun getChat(chat_id: String?, handler: CallbackHandler<Chat>?) {
        if(chat_id==null){
            handler?.onResultEmpty("id not provided")
            return
        }

        chats.child(chat_id).get().addOnSuccessListener {
            val chat = it.getValue(Chat::class.java)
            handler?.onResult(chat)
        }.addOnFailureListener{
            handler?.onResultEmpty("chat with given id not found")
        }
    }

    override fun addChat(chat: Chat?, first_id: String?, second_id: String?, handler: CallbackHandler<Chat>?) {
        if(chat==null){
            handler?.onResultEmpty("chat not provided")
        } else if (chat.messages==null || chat.messages.size != 0){
            handler?.onResultEmpty("Not enough information provided")
        } else{

            if(first_id==null || second_id==null){
                handler?.onResultEmpty("Chat between Users with null id cant be added")
                return
            }

            val id: String = UUID.randomUUID().toString()
            val chatWithId: Chat = chat.withIdAndMessages(id)
            chats.child(id).setValue(chatWithId)

            addChatToUser(first_id, id)
            addChatToUser(second_id, id)

        }
    }

    override fun addMessage(chat_id: String?, message: Message?, handler: CallbackHandler<Message>?) {
        if(chat_id == null){
            handler?.onResultEmpty("No chat provided for message")
        } else if(message == null){
            handler?.onResultEmpty("No message provided")
        } else if(message.sender_id == null || message.receiver_id == null
            || message.text == null || message.timestamp == null){
            handler?.onResultEmpty("Necessary message info is missing")
        } else{


            chats.child(chat_id).get().addOnSuccessListener {
                val chat = it.getValue(Chat::class.java)
                val id: String = UUID.randomUUID().toString()
                val messageWithId: Message = message.withId(id)
                chat?.messages?.add(messageWithId)
                chats.child(chat_id).setValue(chat)

            }.addOnFailureListener{
                handler?.onResultEmpty("Chat with given id not found")
            }

        }
    }

    override fun listenToChat(chat_id: String?, handler: CallbackHandler<Chat>?): ValueEventListener? {
        if(chat_id==null){
            handler?.onResultEmpty("id not provided")
            return null
        }

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages: MutableList<Message>? = snapshot
                    .getValue(object : GenericTypeIndicator<MutableList<Message>>() {})
                handler?.onResult(Chat(messages, chat_id))
            }

            override fun onCancelled(error: DatabaseError) {
                handler?.onResultEmpty(error.message)
            }
        }

        chats.child(chat_id).child(MESSAGES).addValueEventListener(valueEventListener)

        return valueEventListener
    }

    override fun stopListenToChat(chat_id: String?, valueEventListener: ValueEventListener?,
                                  handler: CallbackHandler<Chat>?) {
        if(chat_id==null){
            handler?.onResultEmpty("id not provided")
        } else if (valueEventListener == null) {
            handler?.onResultEmpty("event listener not provided")
        } else {
            chats.child(chat_id).child(MESSAGES).removeEventListener(valueEventListener)
            handler?.onResult(null)
        }
    }

    private fun addChatToUser(user_id: String, chat_id: String){

        usersChats.child(user_id).get().addOnSuccessListener{
            val currentChats = it.getValue(UserChats::class.java)
            currentChats?.chat_ids?.add(chat_id)
            usersChats.child(user_id).setValue(currentChats)
        }.addOnFailureListener{
            val currentChats = UserChats(mutableListOf(chat_id), user_id)
            usersChats.child(user_id).setValue(currentChats)
        }
    }

    companion object {
        private const val MESSAGES: String = "messages"
    }


}