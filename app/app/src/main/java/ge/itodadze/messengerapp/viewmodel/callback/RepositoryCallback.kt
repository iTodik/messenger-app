package ge.itodadze.messengerapp.viewmodel.callback

interface RepositoryCallback<T> {
    fun onSuccess(result: T?)
    fun onFailure(message: String?)
}