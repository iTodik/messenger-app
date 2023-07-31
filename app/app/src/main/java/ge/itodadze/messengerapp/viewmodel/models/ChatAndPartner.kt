package ge.itodadze.messengerapp.viewmodel.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ChatAndPartner(val chatId: String? = null, val partnerId: String? = null)