package ge.itodadze.messengerapp.view.model

import ge.itodadze.messengerapp.viewmodel.models.User

data class ViewUser(val nickname: String?, val profession: String?, val imgUri: String?) {

    companion object {
        fun fromUser(user: User): ViewUser {
            return ViewUser(user.nickname, user.profession, user.imgUri)
        }
     }

}