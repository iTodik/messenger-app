package ge.itodadze.messengerapp.viewmodel

import androidx.lifecycle.ViewModel
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.viewmodel.callback.SignUpCallback
import ge.itodadze.messengerapp.viewmodel.callback.SignUpCheckExistsCallback
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener

class SignUpViewModel(val usersRepository: UsersRepository): ViewModel() {

    fun trySignUp(nickname: String?, passwordHash: String?, profession: String?) {
        usersRepository.get(nickname, SignUpCheckExistsCallback(
            object: CallbackListener {
                override fun onSuccess() {
                    usersRepository.add(
                        User(nickname=nickname, passwordHash=passwordHash, profession=profession),
                    SignUpCallback(object: CallbackListener {
                        override fun onSuccess() {
                            // Sign Up Done
                        }

                        override fun onFailure(message: String?) {
                            // Could Not Sign Up
                        }
                    }))
                }

                override fun onFailure(message: String?) {
                    // User Already Exists
                }

            }
        ))
    }

}