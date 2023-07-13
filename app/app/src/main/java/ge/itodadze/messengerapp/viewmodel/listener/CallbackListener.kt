package ge.itodadze.messengerapp.viewmodel.listener

interface CallbackListener {
    fun onSuccess()
    fun onFailure(message: String?)
}