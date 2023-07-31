package ge.itodadze.messengerapp.domain.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.itodadze.messengerapp.utils.ChatIdGenerator
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import ge.itodadze.messengerapp.viewmodel.models.Chat
import ge.itodadze.messengerapp.viewmodel.models.ChatAndPartner
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


    override fun getUsersChats(user_id: String?, handler: CallbackHandler<UserChats>?) {

        if(user_id==null){
            handler?.onResultEmpty("User id not provided")
            return
        }

        usersChats.child(user_id).get().addOnSuccessListener { snapshot ->
            val chatList = snapshot.getValue(UserChats::class.java)
            handler?.onResult(chatList)
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
        } else if (first_id==null || second_id==null) {
            handler?.onResultEmpty("Chat between Users with null id cant be added")
        } else {

            val id: String = ChatIdGenerator.generate(first_id, second_id)

            chats.child(id).get().addOnSuccessListener {
                if(it.exists() && it.getValue(Chat::class.java) != null) {
                    handler?.onResult(it.getValue(Chat::class.java))
                } else {
                    val chatWithId: Chat = chat.withIdAndMessages(id)
                    chats.child(id).setValue(chatWithId)

                    addChatToUser(first_id, second_id, id, handler)
                    addChatToUser(second_id, first_id, id, handler)
                    handler?.onResult(chatWithId)
                }
            }.addOnFailureListener{
                handler?.onResultEmpty("chat could not be created due to database error")
            }
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
                var chat = it.getValue(Chat::class.java)
                if (chat == null) {
                    handler?.onResultEmpty("Could not retrieve chat")
                } else {
                    val id: String = UUID.randomUUID().toString()
                    val messageWithId: Message = message.withId(id)
                    if (chat.messages == null) {
                        chat = Chat(
                            mutableListOf(messageWithId), chat.identifier)
                    } else {
                        chat.messages!!.add(messageWithId)
                    }
                    chats.child(chat_id).setValue(chat)
                    handler?.onResult(messageWithId)
                }
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

    private fun addChatToUser(user_id: String, partner_id: String, chat_id: String, handler: CallbackHandler<Chat>?){

        usersChats.child(user_id).get().addOnSuccessListener{
            var currentChats = it.getValue(UserChats::class.java)
            if (currentChats == null) currentChats = UserChats(ArrayList(), user_id)
            currentChats.chat_ids!!.add(ChatAndPartner(chat_id, partner_id))
            usersChats.child(user_id).setValue(currentChats)
        }.addOnFailureListener{
            handler?.onResultEmpty("trouble registering chat for user")
        }
    }

    companion object {
        private const val MESSAGES: String = "messages"
    }


}