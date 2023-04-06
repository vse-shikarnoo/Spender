package com.example.spender.data.firebase.viewModels

import androidx.lifecycle.*
import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.FirebaseRepositoriesHolder
import com.example.spender.data.firebase.repositories.AuthManagerRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class AuthManagerViewModel @Inject constructor() : ViewModel() {
    private val _signInFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val signInFirebaseCallResult: LiveData<FirebaseCallResult<String>> = _signInFirebaseCallResult

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signInFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.authManagerRepository.signIn(
                    email,
                    password
                )
            )
        }
    }

    private val _signUpFirebaseCallResult = MutableLiveData<FirebaseCallResult<FirebaseUser>>()
    val signUpFirebaseCallResult: LiveData<FirebaseCallResult<FirebaseUser>> =
        _signUpFirebaseCallResult

    fun signUp(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signUpFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.authManagerRepository.signUp(
                    email,
                    password
                )
            )
        }
    }

    private val _isEmailVerifiedFirebaseCallResult = MutableLiveData<FirebaseCallResult<Boolean>>()
    val isEmailVerifiedFirebaseCallResult: LiveData<FirebaseCallResult<Boolean>> =
        _isEmailVerifiedFirebaseCallResult

    fun isEmailVerified() {
        viewModelScope.launch(Dispatchers.IO) {
            _isEmailVerifiedFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.authManagerRepository.isEmailVerified()
            )
        }
    }

    private val _currentUser = MutableLiveData<FirebaseCallResult<FirebaseUser>>()
    val currentUser: LiveData<FirebaseCallResult<FirebaseUser>> = _currentUser

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentUser.postValue(
                FirebaseRepositoriesHolder.authManagerRepository.getCurrentUser()
            )
        }
    }

    private val _verifyEmailFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val verifyEmailFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _verifyEmailFirebaseCallResult

    fun verifyEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _verifyEmailFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.authManagerRepository.verifyEmail()
            )
        }
    }

    private val _signOutFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val signOutFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _signOutFirebaseCallResult

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            _signOutFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.authManagerRepository.signOut()
            )
        }
    }
}
