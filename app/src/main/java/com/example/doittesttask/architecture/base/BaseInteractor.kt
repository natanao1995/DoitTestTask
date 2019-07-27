package com.example.doittesttask.architecture.base

import com.example.doittesttask.data.remote.entity.ErrorBody
import com.google.gson.Gson
import retrofit2.Response
import java.lang.Exception

open class BaseInteractor {
    suspend fun <T : Any> processRequest(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call.invoke()

            if (response.isSuccessful)
                ResultSuccess(response.body()!!)
            else {
                handleErrorResponse(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ResultError(e)
        }
    }

    private fun <T : Any> handleErrorResponse(responseString: String?): ResultError<T> {
        val errorEntity = Gson().fromJson(responseString, ErrorBody::class.java)
        return ResultError(exception = Exception(errorEntity?.message))
    }
}