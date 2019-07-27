package com.example.doittesttask.feature.signin

import androidx.lifecycle.MutableLiveData
import com.example.doittesttask.architecture.base.*
import com.example.doittesttask.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SigninViewModel(
    private val user: User,
    private val interactor: SigninInteractor
) : BaseViewModel() {

    val signinModeLiveData = MutableLiveData(Mode.LOG_IN)
    val signinResultLiveData = MutableLiveData<Result<Unit>>()

    fun changeSigninMode(mode: Mode) {
        signinModeLiveData.value = mode
    }

    fun signinUser(email: String, password: String) {
        launch {
            signinResultLiveData.value = ResultLoading()
            val token = when (signinModeLiveData.value) {
                Mode.LOG_IN -> interactor.authorizeUser(email, password)
                Mode.REGISTER -> interactor.registerUser(email, password)
                else -> ResultError(Exception("Signin mode is not specified"))
            }
            if (token is ResultSuccess) {
                user.email = email
                user.password = password
                user.token = token.data.token
            }
            signinResultLiveData.value = token.mapTo {}
        }
    }

    enum class Mode {
        LOG_IN,
        REGISTER
    }
}