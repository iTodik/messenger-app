package ge.itodadze.messengerapp.viewmodel.callback

import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.User

class SearchUsersCallbackHandler(
    private val listener: CallbackListenerWithResult<List<User>>?): CallbackHandler<List<User>> {
    override fun onResult(result: List<User>?) {
        if (result == null || result.isEmpty()) {
            listener?.onFailure("No users found.")
        } else {
            listener?.onSuccess(result)
        }
    }

    override fun onResultEmpty(message: String?) {
        listener?.onFailure(message)
    }
}