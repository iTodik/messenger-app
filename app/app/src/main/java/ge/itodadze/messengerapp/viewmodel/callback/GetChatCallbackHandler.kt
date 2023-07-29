package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.Chat

class GetChatCallbackHandler(private val listener: CallbackListenerWithResult<Chat>?):CallbackHandler<Chat> {
    override fun onResult(result: Chat?) {
        if(result ==null){
            listener?.onFailure("Could not retrieve chat")
        } else{
            listener?.onSuccess(result)
        }
    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}