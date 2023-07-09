package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener

class SignUpCheckExistsCallbackHandler(private val listener: CallbackListener?): CallbackHandler<User> {
    override fun onResult(result: User?) {
        if (result == null) {
            listener?.onSuccess()
        } else {
            listener?.onFailure("User with a given nickname already exists.")
        }
    }

    override fun onResultEmpty(message: String?) {
        listener?.onSuccess()
    }
}