package ge.itodadze.messengerapp.domain.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import java.util.*

class UsersFirebaseRepository(dbUrl: String): UsersRepository {

    private var reference: DatabaseReference = Firebase
        .database(dbUrl)
        .getReference("users")

    override fun get(nickname: String?, handler: CallbackHandler<User>?) {
        if (nickname == null) {
            handler?.onResultEmpty("Nickname not provided.")
            return
        }
        reference.child(nickname).get().addOnSuccessListener {
            if (it.value == null) handler
                ?.onResultEmpty("User with a given nickname not found.")
            handler?.onResult(it.getValue(User::class.java))
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
            val userWithId: User = user.withId(UUID.randomUUID().toString() + user.nickname)
            reference.child(user.nickname).setValue(userWithId)
            handler?.onResult(userWithId)
        }
    }

}