package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.view.model.ViewChat
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.Chat


class GetUsersChatsCallbackHandler(private val listener: CallbackListenerWithResult<MutableList<ViewChat>>?):CallbackHandler<MutableList<ViewChat>> {
    override fun onResult(result: MutableList<ViewChat>?) {
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