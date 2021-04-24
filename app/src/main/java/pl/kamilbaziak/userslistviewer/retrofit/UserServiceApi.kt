package pl.kamilbaziak.userslistviewer.retrofit

import pl.kamilbaziak.userslistviewer.model.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserServiceApi {
    @GET("/users")
    suspend fun getAllUsers() : Response<List<UserModel>>

    @GET("/users/{USERNAME}")
    suspend fun getUserByUserName(@Path("USERNAME") username: String) : Response<UserModel>

}