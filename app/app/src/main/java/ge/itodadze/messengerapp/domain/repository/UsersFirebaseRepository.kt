package ge.itodadze.messengerapp.domain.repository

import android.graphics.Bitmap
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import java.io.ByteArrayOutputStream
import java.util.*

class UsersFirebaseRepository(dbUrl: String): UsersRepository {

    private var users: DatabaseReference = Firebase
        .database(dbUrl)
        .getReference("users")

    private var nicknameToId: DatabaseReference = Firebase
        .database(dbUrl)
        .getReference("nickname_to_id")

    private var images: StorageReference = FirebaseStorage
        .getInstance().getReference("images")

    override fun get(id: String?, handler: CallbackHandler<User>?) {
        if (id == null) {
            handler?.onResultEmpty("Id not provided.")
            return
        }
        users.child(id).get().addOnSuccessListener {
            handler?.onResult(it.getValue(User::class.java))
        }.addOnFailureListener {
            handler?.onResultEmpty("User with a given id not found")
        }
    }

    override fun getByNickname(nickname: String?, handler: CallbackHandler<User>?) {
        if (nickname == null) {
            handler?.onResultEmpty("Nickname not provided.")
            return
        }
        nicknameToId.child(nickname).get().addOnSuccessListener {
            if (it.value == null) {
                handler?.onResultEmpty("User with a given nickname not found.")
            } else {
                users.child(it.getValue(String::class.java)!!).get().addOnSuccessListener {
                    a -> handler?.onResult(a.getValue(User::class.java))
                }.addOnFailureListener {
                    handler?.onResultEmpty("User with a given id not found.")
                }
            }
        }.addOnFailureListener {
            handler?.onResultEmpty("Request failed.")
        }
    }

    override fun add(user: User?, handler: CallbackHandler<User>?) {
        if (user == null) {
            handler?.onResultEmpty("User not provided.")
        } else if (user.nickname == null
            || user.passwordHash == null || user.profession == null) {
            handler?.onResultEmpty("Not enough information provided.")
        } else {
            val id: String = UUID.randomUUID().toString() + user.nickname
            val userWithId: User = user.withId(id)
            users.child(id).setValue(userWithId)
            nicknameToId.child(user.nickname).setValue(id)
            handler?.onResult(userWithId)
        }
    }

    override fun update(id: String?, nickname: String?, profession: String?,
                        img: Bitmap?, handler: CallbackHandler<User>?) {
        if (id == null) {
            handler?.onResultEmpty("User id not provided.")
        } else {
            if (nickname != null) users.child(id).child(NICKNAME).setValue(nickname)
            if (profession != null) users.child(id).child(PROFESSION).setValue(profession)
            if (img != null) {
                val os = ByteArrayOutputStream()
                img.compress(Bitmap.CompressFormat.JPEG, 100, os)
                val bytes: ByteArray = os.toByteArray()
                images.child(id).child(IMG_ID).putBytes(bytes).addOnSuccessListener {
                    handler?.onResult(null)
                }.addOnFailureListener {
                    handler?.onResultEmpty("Failed to upload image to storage.")
                }
            }
        }
    }

    companion object {
        private const val NICKNAME: String = "nickname"
        private const val PROFESSION: String = "profession"
        private const val IMG_ID: String = "image.jpg"
    }

}