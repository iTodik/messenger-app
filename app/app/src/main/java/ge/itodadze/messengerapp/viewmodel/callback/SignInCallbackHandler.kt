package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult

class SignInCallbackHandler(private val initialUser: User,
                            private val listener: CallbackListenerWithResult<User>?):
    CallbackHandler<User> {
    override fun onResult(result: User?) {
        if (result?.identifier == null) {
            listener?.onFailure("Could not access User.")
        } else if (initialUser.passwordHash != result.passwordHash) {
            listener?.onFailure("Incorrect password provided.")
        } else {
            listener?.onSuccess(result)
        }
    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}