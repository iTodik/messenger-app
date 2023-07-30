package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener
import ge.itodadze.messengerapp.viewmodel.models.Message

class AddMessageCallbackHandler (private val listener: CallbackListener?):CallbackHandler<Message> {
    override fun onResult(result: Message?) {
        if (result == null) {
            listener?.onFailure("Message could not be added.")
        } else if(result.identifier == null) {
            listener?.onFailure("Could not generate message id.")
        } else {
            listener?.onSuccess()
        }

    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}