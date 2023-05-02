package com.example.spender.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.DataResult
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.UserName
import com.example.spender.domain.repository.UserRepository
import com.google.common.base.Stopwatch
import com.google.firebase.firestore.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.Timer
import java.util.TimerTask

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: dagger.Lazy<UserRepository>,
    private val appContext: Application
) : ViewModel() {

    /** Geters
     * They do not have showMsg live data variables
     * */

    private val _getUserNameDataResult = MutableLiveData<DataResult<UserName>>()
    val getUserNameDataResult: LiveData<DataResult<UserName>> =
        _getUserNameDataResult

    fun getUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserNameDataResult.postValue(
                repository.get().getUserName(Source.CACHE)
            )
        }.invokeOnCompletion {
            if (InternetChecker.check(appContext)) {
                viewModelScope.launch(Dispatchers.IO) {
                    _getUserNameDataResult.postValue(
                        repository.get().getUserName(Source.SERVER)
                    )
                }
            }
        }
    }

    private val _getUserAgeDataResult = MutableLiveData<DataResult<Long>>()
    val getUserAgeDataResult: LiveData<DataResult<Long>> =
        _getUserAgeDataResult

    fun getUserAge() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserAgeDataResult.postValue(
                repository.get().getUserAge(Source.CACHE)
            )
        }.invokeOnCompletion {
            if (InternetChecker.check(appContext)) {
                viewModelScope.launch(Dispatchers.IO) {
                    _getUserAgeDataResult.postValue(
                        repository.get().getUserAge(Source.SERVER)
                    )
                }
            }
        }
    }

    private val _getUserNicknameDataResult = MutableLiveData<DataResult<String>>()
    val getUserNicknameDataResult: LiveData<DataResult<String>> =
        _getUserNicknameDataResult

    fun getUserNickname() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserNicknameDataResult.postValue(
                repository.get().getUserNickname(Source.CACHE)
            )
        }.invokeOnCompletion {
            if (InternetChecker.check(appContext)) {
                viewModelScope.launch(Dispatchers.IO) {
                    _getUserNicknameDataResult.postValue(
                        repository.get().getUserNickname(Source.SERVER)
                    )
                }
            }
        }
    }

    private val _getUserIncomingFriendsDataResult =
        MutableLiveData<DataResult<List<Friend>>>()
    val getUserIncomingFriendsDataResult: LiveData<DataResult<List<Friend>>> =
        _getUserIncomingFriendsDataResult

    fun getUserIncomingFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserIncomingFriendsDataResult.postValue(
                repository.get().getUserIncomingFriends(Source.CACHE)
            )
        }.invokeOnCompletion {
            if (InternetChecker.check(appContext)) {
                viewModelScope.launch(Dispatchers.IO) {
                    _getUserIncomingFriendsDataResult.postValue(
                        repository.get().getUserIncomingFriends(Source.SERVER)
                    )
                }
            }
        }
    }

    private val _getUserOutgoingFriendsDataResult =
        MutableLiveData<DataResult<List<Friend>>>()
    val getUserOutgoingFriendsDataResult: LiveData<DataResult<List<Friend>>> =
        _getUserOutgoingFriendsDataResult

    fun getUserOutgoingFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserOutgoingFriendsDataResult.postValue(
                repository.get().getUserOutgoingFriends(Source.CACHE)
            )
        }.invokeOnCompletion {
            if (InternetChecker.check(appContext)) {
                viewModelScope.launch(Dispatchers.IO) {
                    _getUserOutgoingFriendsDataResult.postValue(
                        repository.get().getUserOutgoingFriends(Source.SERVER)
                    )
                }
            }
        }
    }

    private val _getUserFriendsDataResult =
        MutableLiveData<DataResult<List<Friend>>>()
    val getUserFriendsDataResult: LiveData<DataResult<List<Friend>>> =
        _getUserFriendsDataResult

    fun getUserFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserFriendsDataResult.postValue(
                repository.get().getUserFriends(Source.CACHE)
            )
        }.invokeOnCompletion {
            if (InternetChecker.check(appContext)) {
                viewModelScope.launch(Dispatchers.IO) {
                    _getUserFriendsDataResult.postValue(
                        repository.get().getUserFriends(Source.SERVER)
                    )
                }
            }
        }
    }

    /** Updaters and adders
     * They have showMsg live data variables that must be reset in viewModelResultHandler
     * function if we want to show messages to user
     * */

    /*
    * updateUserName
     */

    private val _updateUserNameDataResult = MutableLiveData<DataResult<String>>()
    val updateUserNameDataResult: LiveData<DataResult<String>> =
        _updateUserNameDataResult
    private val _updateUserNameMsgShow = MutableLiveData<Boolean>()
    val updateUserNameMsgShow: LiveData<Boolean> = _updateUserNameMsgShow

    fun updateUserName(newName: UserName) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserNameDataResult.postValue(
                repository.get().updateUserName(newName)
            )
        }.invokeOnCompletion {
            _updateUserNameMsgShow.postValue(true)
        }
    }

    fun doNotShowUpdateUserNameMsg() {
        _updateUserNameMsgShow.postValue(false)
    }

    /*
    * updateUserAge
     */

    private val _updateUserAgeDataResult = MutableLiveData<DataResult<String>>()
    val updateUserAgeDataResult: LiveData<DataResult<String>> =
        _updateUserAgeDataResult
    private val _updateUserAgeMsgShow = MutableLiveData<Boolean>()
    val updateUserAgeMsgShow: LiveData<Boolean> = _updateUserAgeMsgShow

    fun updateUserAge(newAge: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserAgeDataResult.postValue(
                repository.get().updateUserAge(newAge)
            )
        }.invokeOnCompletion {
            _updateUserAgeMsgShow.postValue(true)
        }
    }

    fun doNotShowUpdateUserAgeMsg() {
        _updateUserAgeMsgShow.postValue(false)
    }

    /*
    * updateUserNickname
     */

    private val _updateUserNicknameDataResult =
        MutableLiveData<DataResult<String>>()
    val updateUserNicknameDataResult: LiveData<DataResult<String>> =
        _updateUserNicknameDataResult
    private val _updateUserNicknameMsgShow = MutableLiveData<Boolean>()
    val updateUserNicknameMsgShow: LiveData<Boolean> = _updateUserNicknameMsgShow

    fun updateUserNickname(newNickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserNicknameDataResult.postValue(
                repository.get().updateUserNickname(newNickname)
            )
        }.invokeOnCompletion {
            _updateUserNicknameMsgShow.postValue(true)
        }
    }

    fun doNotShowUpdateUserNicknameMsg() {
        _updateUserNicknameMsgShow.postValue(false)
    }

    /*
    * addUserOutgoingFriend
     */

    private val _addUserOutgoingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val addUserOutgoingFriendDataResult: LiveData<DataResult<String>> =
        _addUserOutgoingFriendDataResult
    private val _addUserOutgoingFriendMsgShow = MutableLiveData<Boolean>()
    val addUserOutgoingFriendMsgShow: LiveData<Boolean> = _addUserOutgoingFriendMsgShow

    fun addUserOutgoingFriend(nickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _addUserOutgoingFriendDataResult.postValue(
                repository.get().addUserOutgoingFriend(nickname)
            )
        }.invokeOnCompletion {
            _addUserOutgoingFriendMsgShow.postValue(true)
            getUserOutgoingFriends()
        }
    }

    fun doNotShowAddUserOutgoingFriendMsg() {
        _addUserOutgoingFriendMsgShow.postValue(false)
    }

    /*
    * addUserIncomingFriend
     */

    private val _addUserIncomingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val addUserIncomingFriendDataResult: LiveData<DataResult<String>> =
        _addUserIncomingFriendDataResult
    private val _addUserIncomingFriendMsgShow = MutableLiveData<Boolean>()
    val addUserIncomingFriendMsgShow: LiveData<Boolean> = _addUserIncomingFriendMsgShow

    fun addUserIncomingFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addUserIncomingFriendDataResult.postValue(
                repository.get().addUserIncomingFriend(friend)
            )
        }.invokeOnCompletion {
            _addUserIncomingFriendMsgShow.postValue(true)
            getUserFriends()
            getUserIncomingFriends()
        }
    }

    fun doNotShowAddUserIncomingFriendMsg() {
        _addUserIncomingFriendMsgShow.postValue(false)
    }

    /*
    * removeUserFriend
     */

    private val _removeUserFriendDataResult = MutableLiveData<DataResult<String>>()
    val removeUserFriendDataResult: LiveData<DataResult<String>> =
        _removeUserFriendDataResult
    private val _removeUserFriendMsgShow = MutableLiveData<Boolean>()
    val removeUserFriendMsgShow: LiveData<Boolean> = _removeUserFriendMsgShow

    fun removeUserFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserFriendDataResult.postValue(
                repository.get().removeUserFriend(friend)
            )
        }.invokeOnCompletion {
            _removeUserFriendMsgShow.postValue(true)
            getUserFriends()
        }
    }

    fun doNotShowRemoveUserFriendMsg() {
        _removeUserFriendMsgShow.postValue(false)
    }

    /*
    * removeUserOutgoingFriend
     */

    private val _removeUserOutgoingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val removeUserOutgoingFriendDataResult: LiveData<DataResult<String>> =
        _removeUserOutgoingFriendDataResult
    private val _removeUserOutgoingFriendMsgShow = MutableLiveData<Boolean>()
    val removeUserOutgoingFriendMsgShow: LiveData<Boolean> = _removeUserOutgoingFriendMsgShow

    fun removeUserOutgoingFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserOutgoingFriendDataResult.postValue(
                repository.get().removeUserOutgoingFriend(friend)
            )
        }.invokeOnCompletion {
            _removeUserOutgoingFriendMsgShow.postValue(true)
            getUserOutgoingFriends()
        }
    }

    fun doNotShowRemoveUserOutgoingFriendMsg() {
        _removeUserOutgoingFriendMsgShow.postValue(false)
    }

    /*
    * removeUserIncomingFriend
     */

    private val _removeUserIncomingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val removeUserIncomingFriendDataResult: LiveData<DataResult<String>> =
        _removeUserIncomingFriendDataResult
    private val _removeUserIncomingFriendMsgShow = MutableLiveData<Boolean>()
    val removeUserIncomingFriendMsgShow: LiveData<Boolean> = _removeUserIncomingFriendMsgShow

    fun removeUserIncomingFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserIncomingFriendDataResult.postValue(
                repository.get().removeUserIncomingFriend(friend)
            )
        }.invokeOnCompletion {
            _removeUserIncomingFriendMsgShow.postValue(true)
            getUserIncomingFriends()
        }
    }

    fun doNotShowRemoveUserIncomingFriendMsg() {
        _removeUserIncomingFriendMsgShow.postValue(false)
    }
}
