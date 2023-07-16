package ge.itodadze.messengerapp.viewmodel.listener

interface CallbackListenerWithResult<T> {
    fun onSuccess(result: T)
    fun onFailure(message: String?)
}