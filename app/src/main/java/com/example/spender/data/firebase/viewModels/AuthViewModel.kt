package com.example.spender.data.firebase.viewModels

import androidx.lifecycle.*
import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.repositoryInterfaces.AuthRepositoryInterface
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: dagger.Lazy<AuthRepositoryInterface>
) : ViewModel() {
    private val _signInFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val signInFirebaseCallResult: LiveData<FirebaseCallResult<String>> = _signInFirebaseCallResult

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signInFirebaseCallResult.postValue(
                repository.get().signIn(email, password)
            )
        }
    }

    private val _signUpFirebaseCallResult = MutableLiveData<FirebaseCallResult<FirebaseUser>>()
    val signUpFirebaseCallResult: LiveData<FirebaseCallResult<FirebaseUser>> =
        _signUpFirebaseCallResult

    fun signUp(email: String, password: String, nickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signUpFirebaseCallResult.postValue(
                repository.get().signUp(email, password, nickname)
            )
        }
    }

    private val _isEmailVerifiedFirebaseCallResult = MutableLiveData<FirebaseCallResult<Boolean>>()
    val isEmailVerifiedFirebaseCallResult: LiveData<FirebaseCallResult<Boolean>> =
        _isEmailVerifiedFirebaseCallResult

    fun isEmailVerified() {
        viewModelScope.launch(Dispatchers.IO) {
            _isEmailVerifiedFirebaseCallResult.postValue(
                repository.get().isEmailVerified()
            )
        }
    }

    private val _currentUser = MutableLiveData<FirebaseCallResult<FirebaseUser>>()
    val currentUser: LiveData<FirebaseCallResult<FirebaseUser>> = _currentUser

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentUser.postValue(
                repository.get().getCurrentUser()
            )
        }
    }

    private val _verifyEmailFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val verifyEmailFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _verifyEmailFirebaseCallResult

    fun verifyEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _verifyEmailFirebaseCallResult.postValue(
                repository.get().verifyEmail()
            )
        }
    }

    private val _signOutFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val signOutFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _signOutFirebaseCallResult

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            _signOutFirebaseCallResult.postValue(
                repository.get().signOut()
            )
        }
    }
}
