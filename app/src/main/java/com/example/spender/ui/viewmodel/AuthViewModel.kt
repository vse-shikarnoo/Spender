package com.example.spender.ui.viewmodel

import androidx.lifecycle.*
import com.example.spender.data.DataResult
import com.example.spender.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: dagger.Lazy<AuthRepository>
) : ViewModel() {

    /*
     * Sign in
     */

    private val _signInDataResult = MutableLiveData<DataResult<String>>()
    val signInDataResult: LiveData<DataResult<String>> = _signInDataResult
    private val _signInMsgShow = MutableLiveData<Boolean>()
    val signInMsgShow: LiveData<Boolean> = _signInMsgShow

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signInDataResult.postValue(
                repository.get().signIn(email, password)
            )
        }.invokeOnCompletion {
            _signInMsgShow.postValue(true)
        }
    }

    fun doNotShowSignInMsg() {
        _signInMsgShow.postValue(false)
    }

    /*
     * Sign up
     */

    private val _signUpDataResult = MutableLiveData<DataResult<FirebaseUser>>()
    val signUpDataResult: LiveData<DataResult<FirebaseUser>> =
        _signUpDataResult
    private val _signUpMsgShow = MutableLiveData<Boolean>()
    val signUpMsgShow: LiveData<Boolean> = _signUpMsgShow

    fun signUp(email: String, password: String, nickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signUpDataResult.postValue(
                repository.get().signUp(email, password, nickname)
            )
        }.invokeOnCompletion {
            _signUpMsgShow.postValue(true)
        }
    }

    fun doNotShowSignUpMsg() {
        _signUpMsgShow.postValue(false)
    }

    /*
     * Is email verified
     */

    private val _isEmailVerifiedDataResult = MutableLiveData<DataResult<Boolean>>()
    val isEmailVerifiedDataResult: LiveData<DataResult<Boolean>> =
        _isEmailVerifiedDataResult

    fun isEmailVerified() {
        viewModelScope.launch(Dispatchers.IO) {
            _isEmailVerifiedDataResult.postValue(
                repository.get().isEmailVerified()
            )
        }
    }

    /*
     * Get current user
     */

    private val _currentUser = MutableLiveData<DataResult<FirebaseUser>>()
    val currentUser: LiveData<DataResult<FirebaseUser>> = _currentUser

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentUser.postValue(
                repository.get().getCurrentUser()
            )
        }
    }

    /*
     * Verify email
     */

    private val _verifyEmailDataResult = MutableLiveData<DataResult<String>>()
    val verifyEmailDataResult: LiveData<DataResult<String>> =
        _verifyEmailDataResult
    private val _verifyEmailMsgShow = MutableLiveData<Boolean>()
    val verifyEmailMsgShow: LiveData<Boolean> = _verifyEmailMsgShow

    fun verifyEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _verifyEmailDataResult.postValue(
                repository.get().verifyEmail()
            )
        }.invokeOnCompletion {
            _verifyEmailMsgShow.postValue(true)
        }
    }

    fun doNotShowVerifyEmailMsg() {
        _verifyEmailMsgShow.postValue(false)
    }

    /*
     * Sign out
     */

    private val _signOutDataResult = MutableLiveData<DataResult<String>>()
    val signOutDataResult: LiveData<DataResult<String>> =
        _signOutDataResult

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            _signOutDataResult.postValue(
                repository.get().signOut()
            )
        }
    }
}
