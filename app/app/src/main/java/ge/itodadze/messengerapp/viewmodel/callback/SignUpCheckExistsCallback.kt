package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener

class SignUpCheckExistsCallback(private val listener: CallbackListener?): RepositoryCallback<User> {
    override fun onSuccess(result: User?) {
        if (result == null) {
            onFailure("User not found.")
        } else {
            listener?.onFailure("User with a given nickname already exists.")
        }
    }

    override fun onFailure(message: String?) {
        listener?.onSuccess()
    }
}