package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener
import ge.itodadze.messengerapp.viewmodel.models.User

class UpdateUserCallbackHandler(private val listener: CallbackListener?): CallbackHandler<User> {
    override fun onResult(result: User?) {
        listener?.onSuccess()
    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}