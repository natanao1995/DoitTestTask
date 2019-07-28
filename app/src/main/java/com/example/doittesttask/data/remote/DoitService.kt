package com.example.doittesttask.data.remote

import com.example.doittesttask.data.remote.entity.*
import retrofit2.Response
import retrofit2.http.*

interface DoitService {
    @GET("tasks")
    suspend fun getTasks(
        @Query("page") page: Int? = null,
        @Query("sort") sort: SortQuery? = null
    ): Response<TasksListBody>

    @POST("tasks")
    suspend fun addTask(
        @Body task: TaskRequestBody
    ): Response<TaskResponseBody>

    @GET("tasks/{id}")
    suspend fun getTaskById(
        @Path("id") id: Long
    ): Response<TaskResponseBody>

    @PUT("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: Long,
        @Body task: TaskRequestBody
    ): Response<Unit>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(
        @Path("id") id: Long
    ): Response<Unit>

    @POST("users")
    suspend fun registerUser(
        @Body user: UserBody
    ): Response<TokenBody>

    @POST("auth")
    suspend fun authorizeUser(
        @Body user: UserBody
    ): Response<TokenBody>
}