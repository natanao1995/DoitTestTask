package com.example.doittesttask.feature.signin

import androidx.lifecycle.MutableLiveData
import com.example.doittesttask.architecture.base.*
import com.example.doittesttask.architecture.exception.SigninModeNotSpecifiedException
import com.example.doittesttask.feature.auth.AuthManager
import kotlinx.coroutines.launch

class SigninViewModel(
    private val interactor: SigninInteractor,
    private val authManager: AuthManager
) : BaseViewModel() {

    val signinModeLiveData = MutableLiveData(Mode.LOG_IN)
    val signinResultLiveData = MutableLiveData<Result<Unit>>()
    val silentAuthResultLiveData = MutableLiveData<Result<Unit>>()

    fun changeSigninMode(mode: Mode) {
        signinModeLiveData.value = mode
    }

    fun silentAuth() {
        launch {
            silentAuthResultLiveData.value = ResultLoading()
            silentAuthResultLiveData.value = authManager.silentAuth()
        }
    }

    fun clearToken() {
        launch {
            authManager.clearToken()
        }
    }

    fun signinUser(email: String, password: String) {
        launch {
            signinResultLiveData.value = ResultLoading()
            val result = when (signinModeLiveData.value) {
                Mode.LOG_IN -> interactor.authorizeUser(email, password)
                Mode.REGISTER -> interactor.registerUser(email, password)
                else -> ResultError(SigninModeNotSpecifiedException())
            }
            if (result is ResultSuccess) {
                authManager.signin(result.data.token)
            }
            signinResultLiveData.value = result.mapTo {}
        }
    }

    enum class Mode {
        LOG_IN,
        REGISTER
    }
}