package ge.itodadze.messengerapp.domain.repository

import android.graphics.Bitmap
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler

interface UsersRepository {
    fun get(id: String?, handler: CallbackHandler<User>?)
    fun getByNickname(nickname: String?, handler: CallbackHandler<User>?)
    fun add(user: User?, handler: CallbackHandler<User>?)
    fun update(id: String?, nickname: String?, profession: String?, img: Bitmap?, handler: CallbackHandler<User>?)
}