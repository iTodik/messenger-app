package ge.itodadze.messengerapp.domain.repository

import android.content.res.Resources
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.callback.RepositoryCallback

class UsersFirebaseRepository: UsersRepository {

    private var reference: DatabaseReference = Firebase
        .database(Resources.getSystem().getString(R.string.db_location))
        .getReference("users")

    override fun get(nickname: String?, callback: RepositoryCallback<User>?) {
        if (nickname == null) {
            callback?.onFailure("Nickname not provided.")
            return
        }
        reference.child(nickname).get().addOnSuccessListener {
            callback?.onSuccess(it.value as User?)
        }.addOnFailureListener {
            callback?.onFailure("User with a given nickname not found.")
        }
    }

    override fun add(user: User?, callback: RepositoryCallback<User>?) {
        if (user == null) {
            callback?.onFailure("User not provided.")
        } else if (user.nickname == null
            || user.passwordHash == null || user.profession == null) {
            callback?.onFailure("Not enough information provided.")
        } else {
            reference.child(user.nickname).setValue(user)
            callback?.onSuccess(user)
        }
    }

}