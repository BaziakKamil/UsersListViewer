package pl.kamilbaziak.userslistviewer.ui.pickeduserfragment

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.kamilbaziak.userslistviewer.model.UserModel
import pl.kamilbaziak.userslistviewer.repository.UserRepository
import pl.kamilbaziak.userslistviewer.ui.alluserfragment.AllUserFragmentViewModel
import javax.inject.Inject

@HiltViewModel
class PickedUserFragmentViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val userRepository: UserRepository
): ViewModel() {

    val progressVisible = ObservableField(true)

    private val pickedUserChannel =
        Channel<PickedUserListEvent>()
    val pickedUserEvent = pickedUserChannel.receiveAsFlow()

    private val pickedUserModel = state.get<UserModel>("userModel")
    val pickedUserUsername = pickedUserModel?.userName ?: ""
    private val pickedUserAvatarUrl = pickedUserModel?.avatarUrl ?: ""

    private val _pickedUserWithMoreDetails = MutableLiveData<UserModel?>()
    val pickedUserWithMoreDetails = _pickedUserWithMoreDetails

    fun getUserByUsername(){
        //showing progressbar
        progressVisible.set(true)

        viewModelScope.launch {
            val response = userRepository.getUserByUserName(pickedUserUsername)
            if (response.isSuccessful) {
                val userResponseBody = response.body()
                _pickedUserWithMoreDetails.value = userResponseBody
                pickedUserChannel.send(PickedUserListEvent.BindDataToViews)
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
            //hiding progressbar
            progressVisible.set(false)
        }
    }

    fun loadImageWithUri(imageView: ImageView){
        val url = pickedUserAvatarUrl
        if(url.isNotEmpty())
            Glide.with(imageView.context).load(Uri.parse(url)).into(imageView)
        //TODO show dialof
    }

    //for "communication" with fragment purposes
    sealed class PickedUserListEvent {
        object BindDataToViews : PickedUserListEvent()
    }
}