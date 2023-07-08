package ge.itodadze.messengerapp.domain.repository

import android.content.res.Resources
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.data.models.User
import ge.itodadze.messengerapp.presenter.listeners.UsersRepositoryListener

class UsersFirebaseRepository(private val listener: UsersRepositoryListener): UsersRepository {

    private var reference: DatabaseReference = Firebase
        .database(Resources.getSystem().getString(R.string.db_location))
        .getReference("users")

    override fun requestUsername(user: User) {
        if (user.nickname == null) return
        reference.child(user.nickname).get().addOnSuccessListener{
            listener.onUsername(it.value as User?)
        }.addOnFailureListener {
            listener.onUsername(null)
        }
    }

    override fun requestUsernamePassword(user: User) {
        if (user.nickname == null || user.passwordHash == null) return
        reference.child(user.nickname).get().addOnSuccessListener{
            if ((it.value as User?)?.passwordHash == user.passwordHash) {
                listener.onUsernamePassword(it.value as User?)
            }
            listener.onUsernamePassword(null)
        }.addOnFailureListener{
            listener.onUsernamePassword(null)
        }
    }

    override fun add(user: User): Boolean {
        if (user.nickname == null) return false
        reference.child(user.nickname).setValue(user)
        return true
    }
}