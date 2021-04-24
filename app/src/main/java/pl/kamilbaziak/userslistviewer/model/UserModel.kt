package pl.kamilbaziak.userslistviewer.model

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    @SerializedName("login") val userName: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val homeUrl: String,
    val id: Int,
    val name: String,
    val blog: String,
    val location: String,
    val followers: Int
): Parcelable