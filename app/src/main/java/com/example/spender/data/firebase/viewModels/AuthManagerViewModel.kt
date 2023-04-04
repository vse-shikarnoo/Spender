package com.example.spender.data.firebase.viewModels

//import android.widget.Toast
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.Button
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.repositories.AuthManagerRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthManagerViewModel @Inject constructor() : ViewModel() {
    private val repository = AuthManagerRepository()

    private val _signInResult = MutableLiveData<Result<Boolean>>()
    val signInResult: LiveData<Result<Boolean>> = _signInResult

    private val _signUpResult = MutableLiveData<Result<FirebaseUser>>()
    val signUpResult: LiveData<Result<FirebaseUser>> = _signUpResult

    private val _isEmailVerifiedResult = MutableLiveData<Result<Boolean>>()
    val isEmailVerifiedResult: LiveData<Result<Boolean>> = _isEmailVerifiedResult

    private val _currentUser = MutableLiveData<Result<FirebaseUser>>()
    val currentUser: LiveData<Result<FirebaseUser>> = _currentUser

    private val _verifyEmailResult = MutableLiveData<Result<Boolean>>()
    val verifyEmailResult: LiveData<Result<Boolean>> = _verifyEmailResult

    private val _resetPasswordResult = MutableLiveData<Result<Boolean>>()
    val resetPasswordResult: LiveData<Result<Boolean>> = _resetPasswordResult

    private val _signOutResult = MutableLiveData<Result<Boolean>>()
    val signOutResult: LiveData<Result<Boolean>> = _signOutResult

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signInResult.postValue(repository.signIn(email, password))
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signUpResult.postValue(repository.signUp(email, password))
        }
    }

    fun isEmailVerified() {
        viewModelScope.launch(Dispatchers.IO) {
            _isEmailVerifiedResult.postValue(repository.isEmailVerified())
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentUser.postValue(repository.getCurrentUser())
        }
    }

    fun verifyEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _verifyEmailResult.postValue(repository.verifyEmail())
        }
    }

    fun resetPassword() {
        viewModelScope.launch(Dispatchers.IO) {
            _resetPasswordResult.postValue(repository.resetPassword())
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            _signOutResult.postValue(repository.signOut())
        }
    }
}
