package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.Chat


class GetLastChatsCallbackHandler(private val listener: CallbackListenerWithResult<List<Chat>>?):CallbackHandler<List<Chat>> {
    override fun onResult(result: List<Chat>?) {
        if (result == null) {
            listener?.onFailure("Could not retrieve active chats.")
        } else {
            listener?.onSuccess(result)
        }
    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}