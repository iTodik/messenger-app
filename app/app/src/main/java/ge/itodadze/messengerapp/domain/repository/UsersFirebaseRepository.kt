package ge.itodadze.messengerapp.domain.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import java.util.*

class UsersFirebaseRepository(dbUrl: String): UsersRepository {

    private var users: DatabaseReference = Firebase
        .database(dbUrl)
        .getReference("users")

    private var nicknameToId: DatabaseReference = Firebase
        .database(dbUrl)
        .getReference("nickname_to_id")

    override fun get(id: String?, handler: CallbackHandler<User>?) {
        if (id == null) {
            handler?.onResultEmpty("Id not provided.")
            return
        }
        users.child(id).get().addOnSuccessListener {
            handler?.onResult(it.getValue(User::class.java))
        }.addOnFailureListener {
            handler?.onResultEmpty("User with a given id not found")
        }
    }

    override fun getByNickname(nickname: String?, handler: CallbackHandler<User>?) {
        if (nickname == null) {
            handler?.onResultEmpty("Nickname not provided.")
            return
        }
        nicknameToId.child(nickname).get().addOnSuccessListener {
            if (it.value == null) {
                handler?.onResultEmpty("User with a given nickname not found.")
            } else {
                users.child(it.getValue(String::class.java)!!).get().addOnSuccessListener {
                    a -> handler?.onResult(a.getValue(User::class.java))
                }.addOnFailureListener {
                    handler?.onResultEmpty("User with a given id not found.")
                }
            }
        }.addOnFailureListener {
            handler?.onResultEmpty("Request failed.")
        }
    }

    override fun add(user: User?, handler: CallbackHandler<User>?) {
        if (user == null) {
            handler?.onResultEmpty("User not provided.")
        } else if (user.nickname == null
            || user.passwordHash == null || user.profession == null) {
            handler?.onResultEmpty("Not enough information provided.")
        } else {
            val id: String = UUID.randomUUID().toString() + user.nickname
            val userWithId: User = user.withId(id)
            users.child(id).setValue(userWithId)
            nicknameToId.child(user.nickname).setValue(id)
            handler?.onResult(userWithId)
        }
    }

}