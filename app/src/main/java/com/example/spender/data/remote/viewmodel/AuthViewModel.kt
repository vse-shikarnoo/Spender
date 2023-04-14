package com.example.spender.data.remote.viewmodel

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
    private val _signInDataResult = MutableLiveData<DataResult<String>>()
    val signInDataResult: LiveData<DataResult<String>> = _signInDataResult

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signInDataResult.postValue(
                repository.get().signIn(email, password)
            )
        }
    }

    private val _signUpDataResult = MutableLiveData<DataResult<FirebaseUser>>()
    val signUpDataResult: LiveData<DataResult<FirebaseUser>> =
        _signUpDataResult

    fun signUp(email: String, password: String, nickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signUpDataResult.postValue(
                repository.get().signUp(email, password, nickname)
            )
        }
    }

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

    private val _currentUser = MutableLiveData<DataResult<FirebaseUser>>()
    val currentUser: LiveData<DataResult<FirebaseUser>> = _currentUser

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentUser.postValue(
                repository.get().getCurrentUser()
            )
        }
    }

    private val _verifyEmailDataResult = MutableLiveData<DataResult<String>>()
    val verifyEmailDataResult: LiveData<DataResult<String>> =
        _verifyEmailDataResult

    fun verifyEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _verifyEmailDataResult.postValue(
                repository.get().verifyEmail()
            )
        }
    }

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
