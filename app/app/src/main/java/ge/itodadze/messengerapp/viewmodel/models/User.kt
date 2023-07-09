package ge.itodadze.messengerapp.viewmodel.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val nickname: String? = null, val passwordHash: String? = null, val profession: String? = null) {
}