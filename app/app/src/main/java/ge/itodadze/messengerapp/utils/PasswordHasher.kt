package ge.itodadze.messengerapp.utils

import java.security.MessageDigest

class PasswordHasher {
    companion object {
        fun sha1(txt: String): String {
            val bytes = MessageDigest.getInstance("SHA-1").digest(txt.toByteArray())
            return bytes.joinToString("") {
                "%02x".format(it)
            }
        }
    }
}