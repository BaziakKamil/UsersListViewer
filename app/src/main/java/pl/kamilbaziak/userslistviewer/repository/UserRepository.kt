package pl.kamilbaziak.userslistviewer.repository

import pl.kamilbaziak.userslistviewer.retrofit.UserServiceApi
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userServiceApi: UserServiceApi
) {

    suspend fun getAllUser() = userServiceApi.getAllUsers()

    suspend fun getUserByUserName(name: String) = userServiceApi.getUserByUserName(name)
}