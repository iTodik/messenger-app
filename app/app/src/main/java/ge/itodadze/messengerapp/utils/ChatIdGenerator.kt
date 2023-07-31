package ge.itodadze.messengerapp.utils

class ChatIdGenerator {
    companion object {
        fun generate(firstUser: String, secondUser: String): String {
            return if (firstUser <= secondUser) {
                firstUser + SEPARATOR + secondUser
            } else {
                secondUser + SEPARATOR + firstUser
            }
        }
        private const val SEPARATOR: String = "_CONVERSATION_"
    }
}