package ge.itodadze.messengerapp.data.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val nickname: String?, val passwordHash: String?, val profession: String?) {
}