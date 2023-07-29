package ge.itodadze.messengerapp.domain.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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


    //override fun getUsersLastChats(user_id: String?, handler: CallbackHandler<List<Chat>>?) {

    //}

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

            // rest of message null handling is taken care of in add message
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




}