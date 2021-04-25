package pl.kamilbaziak.userslistviewer.ui.alluserfragment

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.kamilbaziak.userslistviewer.model.UserModel
import pl.kamilbaziak.userslistviewer.repository.UserRepository
import pl.kamilbaziak.userslistviewer.utils.UserListViewerApplication
import javax.inject.Inject

@HiltViewModel
class AllUserFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val progressVisible = MutableLiveData<Boolean>()

    private val allUserChannel =
        Channel<AllUserListEvent>()
    val allUserEvent = allUserChannel.receiveAsFlow()

    //observable list of users
    private val _userList = MutableLiveData<List<UserModel>?>()
    val userList = _userList

    //getting users from repo and from  and etting value of _userList
    fun getUsersFromRepo(){
        //showing progressbar
        progressVisible.value = true

        viewModelScope.launch {
            //the GET request from repo -> from retrofit service and get response
            val response = userRepository.getAllUser()
            if (response.isSuccessful) {
                val list = response.body()
                _userList.value = list
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }

            //hiding progressbar
            progressVisible.value = false
        }
    }

    //navigating to picked user and passing userModel
    fun navigateToPickedUserFragment(userModel: UserModel) = viewModelScope.launch {
        allUserChannel.send(
            AllUserListEvent.NavigateToPickedUserFragment(
                userModel
            )
        )
    }

    //for "communication" with fragment purposes
    sealed class AllUserListEvent {
        data class NavigateToPickedUserFragment(val userModel: UserModel) :
            AllUserListEvent()
    }
}