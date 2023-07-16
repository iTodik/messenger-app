package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.User

class GetUserCallbackHandler(private val listener: CallbackListenerWithResult<User>?): CallbackHandler<User> {
    override fun onResult(result: User?) {
        if (result == null) {
            listener?.onFailure("Could not retrieve user.")
        } else {
            listener?.onSuccess(result)
        }
    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}