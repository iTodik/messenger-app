package ge.itodadze.messengerapp.data.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class SigningRequest(val nickname: String? = null,
                          val passwordHash: String? = null,
                          val profession: String? = null)