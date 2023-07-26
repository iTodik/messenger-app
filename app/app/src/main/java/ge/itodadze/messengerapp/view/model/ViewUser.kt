package ge.itodadze.messengerapp.view.model

import android.net.Uri
import ge.itodadze.messengerapp.viewmodel.models.User

data class ViewUser(val nickname: String?, val profession: String?, val imgUri: Uri?) {

    companion object {
        fun fromUser(user: User): ViewUser {
            return ViewUser(user.nickname, user.profession, user.imgUri)
        }
     }

}