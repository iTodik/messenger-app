package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.Message

class AddMessageCallbackHandler (private val listener: CallbackListenerWithResult<Message>?):CallbackHandler<Message> {
    override fun onResult(result: Message?) {
        if (result == null) {
            listener?.onFailure("Message could not be added.")
        } else if(result.identifier == null) {
            listener?.onFailure("Could not generate message id.")
        } else {
            listener?.onSuccess(result)
        }

    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}