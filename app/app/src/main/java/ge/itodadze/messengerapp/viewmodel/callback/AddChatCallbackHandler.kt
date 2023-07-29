package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.Chat

class AddChatCallbackHandler (private val listener: CallbackListenerWithResult<Chat>?):CallbackHandler<Chat> {

    override fun onResult(result: Chat?) {
        if (result == null) {
            listener?.onFailure("Chat could not be added.")
        } else if(result.identifier == null) {
            listener?.onFailure("Could not generate chat id.")
        } else {
            listener?.onSuccess(result)
        }

    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}