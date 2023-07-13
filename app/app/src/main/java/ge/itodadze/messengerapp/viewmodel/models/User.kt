package ge.itodadze.messengerapp.viewmodel.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val nickname: String? = null, val passwordHash: String? = null,
                val profession: String? = null, val identifier: String? = null) {
    fun withId(identifier: String): User {
        return User(nickname, passwordHash, profession, identifier)
    }

}