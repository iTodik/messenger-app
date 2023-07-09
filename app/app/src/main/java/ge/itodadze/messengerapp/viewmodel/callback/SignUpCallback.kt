package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener

class SignUpCallback(private val listener: CallbackListener?): RepositoryCallback<User> {
    override fun onSuccess(result: User?) {
        if (result == null) {
            onFailure("User could not be added.")
        } else {
            listener?.onSuccess()
        }
    }

    override fun onFailure(message: String?) {
        listener?.onFailure(message)
    }
}