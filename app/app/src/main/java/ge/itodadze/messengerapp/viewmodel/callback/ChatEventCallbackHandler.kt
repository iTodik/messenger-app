package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.Chat

class ChatEventCallbackHandler(private val listener: CallbackListenerWithResult<Chat>?): CallbackHandler<Chat> {
    override fun onResult(result: Chat?) {
        if (result == null) {
            listener?.onFailure("problem retrieving chat with given id")
        } else {
            listener?.onSuccess(result)
        }
    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}