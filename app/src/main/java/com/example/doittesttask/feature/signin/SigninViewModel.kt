package com.example.doittesttask.feature.signin

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.doittesttask.Constants.TOKEN_KEY
import com.example.doittesttask.architecture.base.*
import com.example.doittesttask.architecture.exception.SigninModeNotSpecifiedException
import com.example.doittesttask.data.User
import de.adorsys.android.securestoragelibrary.SecurePreferences
import kotlinx.coroutines.launch

class SigninViewModel(
    private val context: Context,
    private val user: User,
    private val interactor: SigninInteractor
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
            val token = SecurePreferences.getStringValue(context, TOKEN_KEY, null)

            if (token == null) {
                silentAuthResultLiveData.value = ResultError(Exception())
            } else {
                user.token = token
                silentAuthResultLiveData.value = ResultSuccess(Unit)
            }
        }
    }

    fun clearToken() {
        launch {
            SecurePreferences.removeValue(context, TOKEN_KEY)
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
                user.token = result.data.token

                SecurePreferences.setValue(context, TOKEN_KEY, result.data.token)
            }
            signinResultLiveData.value = result.mapTo {}
        }
    }

    enum class Mode {
        LOG_IN,
        REGISTER
    }
}