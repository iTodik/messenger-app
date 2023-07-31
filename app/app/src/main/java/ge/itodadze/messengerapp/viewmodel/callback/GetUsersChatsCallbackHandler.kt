package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.UserChats


class GetUsersChatsCallbackHandler(private val listener: CallbackListenerWithResult<UserChats>?):CallbackHandler<UserChats> {
    override fun onResult(result: UserChats?) {
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