package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult

class SignUpCallbackHandler(private val listener: CallbackListenerWithResult<User>?): CallbackHandler<User> {
    override fun onResult(result: User?) {
        if (result == null) {
            listener?.onFailure("User could not be added.")
        } else if(result.identifier == null) {
            listener?.onFailure("Could not generate id.")
        } else {
            listener?.onSuccess(result)
        }
    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}