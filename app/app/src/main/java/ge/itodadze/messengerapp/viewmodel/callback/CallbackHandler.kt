package ge.itodadze.messengerapp.viewmodel.callback

interface CallbackHandler<T> {
    fun onResult(result: T?)
    fun onResultEmpty(message: String?)
}