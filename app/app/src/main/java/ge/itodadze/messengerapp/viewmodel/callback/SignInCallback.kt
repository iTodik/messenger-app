package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener

class SignInCallback(private val initialUser: User, private val listener: CallbackListener?): RepositoryCallback<User> {
    override fun onSuccess(result: User?) {
        if (result == null) {
            onFailure("Could not access User.")
        } else if (initialUser.passwordHash != result.passwordHash) {
            onFailure("Incorrect password provided.")
        } else {
            listener?.onSuccess()
        }
    }

    override fun onFailure(message: String?) {
        listener?.onFailure(message)
    }
}