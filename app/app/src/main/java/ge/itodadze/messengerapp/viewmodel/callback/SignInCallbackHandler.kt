package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener

class SignInCallbackHandler(private val initialUser: User, private val listener: CallbackListener?):
    CallbackHandler<User> {
    override fun onResult(result: User?) {
        if (result == null) {
            listener?.onFailure("Could not access User.")
        } else if (initialUser.passwordHash != result.passwordHash) {
            listener?.onFailure("Incorrect password provided.")
        } else {
            listener?.onSuccess()
        }
    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}