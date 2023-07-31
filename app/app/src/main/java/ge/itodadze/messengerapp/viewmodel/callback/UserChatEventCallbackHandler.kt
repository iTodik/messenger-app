package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.UserChats

class UserChatEventCallbackHandler(val listener: CallbackListenerWithResult<UserChats>?): CallbackHandler<UserChats> {
    override fun onResult(result: UserChats?) {
        if (result == null) {
            listener?.onFailure("no chats found")
        } else {
            listener?.onSuccess(result)
        }
    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}