package com.example.doittesttask.feature.signin

import com.example.doittesttask.architecture.base.BaseInteractor
import com.example.doittesttask.architecture.base.Result
import com.example.doittesttask.data.remote.DoitService
import com.example.doittesttask.data.remote.entity.TokenBody
import com.example.doittesttask.data.remote.entity.UserBody

class SigninInteractor(
    private val doitService: DoitService
) : BaseInteractor() {
    suspend fun registerUser(email: String, password: String): Result<TokenBody> {
        return processRequest {
            doitService.registerUser(UserBody(email, password))
        }
    }

    suspend fun authorizeUser(email: String, password: String): Result<TokenBody>/* = withContext(Dispatchers.IO)*/ {
        return processRequest {
            doitService.authorizeUser(UserBody(email, password))
        }
    }
}