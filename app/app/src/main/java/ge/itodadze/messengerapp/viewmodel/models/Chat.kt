package ge.itodadze.messengerapp.viewmodel.models

data class Chat(val sender_id: String?=null, val receiver_id: String?=null, val messages: List<List<String>>?=null) {}