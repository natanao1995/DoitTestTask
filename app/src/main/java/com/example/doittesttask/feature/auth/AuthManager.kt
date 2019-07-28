package com.example.doittesttask.feature.auth

import android.content.Context
import android.content.Intent
import com.example.doittesttask.Constants.SECURE_PREFERENCES_KEY_TOKEN
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.architecture.base.ResultError
import com.example.doittesttask.architecture.base.ResultSuccess
import com.example.doittesttask.data.User
import com.example.doittesttask.feature.signin.SigninActivity
import de.adorsys.android.securestoragelibrary.SecurePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthManager(
    private val context: Context,
    private val user: User
) {
    suspend fun signin(token: String) = withContext(Dispatchers.IO) {
        user.token = token
        SecurePreferences.setValue(context, SECURE_PREFERENCES_KEY_TOKEN, token)
    }

    suspend fun silentAuth(): Result<Unit> = withContext(Dispatchers.IO) {
        val token = SecurePreferences.getStringValue(context, SECURE_PREFERENCES_KEY_TOKEN, null)

        return@withContext if (token == null) {
            ResultError<Unit>(Exception())
        } else {
            user.token = token
            ResultSuccess(Unit)
        }
    }

    suspend fun logout(fromUser: Boolean = false) = withContext(Dispatchers.IO) {
        user.token = null
        SecurePreferences.removeValue(context, SECURE_PREFERENCES_KEY_TOKEN)
        val intent = Intent()
        if (!fromUser) intent.putExtra(SigninActivity.EXTRA_KEY_UNAUTHORIZED, true)
        intent.setClass(context, SigninActivity::class.java)
        intent.action = SigninActivity::class.java.name
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK

        withContext(Dispatchers.Main) {
            context.startActivity(intent)
        }
    }

    suspend fun clearToken() = withContext(Dispatchers.IO) {
        user.token = null
        SecurePreferences.removeValue(context, SECURE_PREFERENCES_KEY_TOKEN)
    }
}