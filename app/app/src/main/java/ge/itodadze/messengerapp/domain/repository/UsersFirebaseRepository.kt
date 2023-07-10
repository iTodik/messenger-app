package ge.itodadze.messengerapp.domain.repository

import android.content.res.Resources
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import java.util.*

class UsersFirebaseRepository: UsersRepository {

    private var reference: DatabaseReference = Firebase
        .database(Resources.getSystem().getString(R.string.db_location))
        .getReference("users")

    override fun get(nickname: String?, handler: CallbackHandler<User>?) {
        if (nickname == null) {
            handler?.onResultEmpty("Nickname not provided.")
            return
        }
        reference.child(nickname).get().addOnSuccessListener {
            handler?.onResult(it.value as User?)
        }.addOnFailureListener {
            handler?.onResultEmpty("User with a given nickname not found.")
        }
    }

    override fun add(user: User?, handler: CallbackHandler<User>?) {
        if (user == null) {
            handler?.onResultEmpty("User not provided.")
        } else if (user.nickname == null
            || user.passwordHash == null || user.profession == null) {
            handler?.onResultEmpty("Not enough information provided.")
        } else {
            reference.child(user.nickname).setValue(user.withId(UUID.randomUUID().toString()))
            handler?.onResult(user)
        }
    }

}