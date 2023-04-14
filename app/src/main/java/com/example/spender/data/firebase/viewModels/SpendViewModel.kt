package com.example.spender.data.firebase.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.repositoryInterfaces.SpendRepositoryInterface
import com.example.spender.data.models.spend.Spend
import com.example.spender.data.models.spend.SpendMember
import com.example.spender.data.models.spend.SplitMode
import com.example.spender.data.models.user.Friend
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SpendViewModel @Inject constructor(
    private val repository: dagger.Lazy<SpendRepositoryInterface>
) : ViewModel() {
    private val _createSpendFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val createSpendFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _createSpendFirebaseCallResult

    suspend fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String,
        splitMode: SplitMode,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _createSpendFirebaseCallResult.postValue(
                repository.get().createSpend(
                    tripDocRef,
                    name,
                    category,
                    splitMode,
                    amount,
                    geoPoint,
                    members
                )
            )
        }
    }

    private val _updateSpendNameFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val updateSpendNameFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _updateSpendNameFirebaseCallResult

    suspend fun updateSpendName(
        spend: Spend,
        newName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateSpendNameFirebaseCallResult.postValue(
                repository.get().updateSpendName(spend, newName)
            )
        }
    }

    private val _updateSpendCategoryFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val updateSpendCategoryFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _updateSpendCategoryFirebaseCallResult

    suspend fun updateSpendCategory(
        spend: Spend,
        newCategory: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateSpendCategoryFirebaseCallResult.postValue(
                repository.get().updateSpendName(spend, newCategory)
            )
        }
    }

    private val _updateSpendSplitModeFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val updateSpendSplitModeFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _updateSpendSplitModeFirebaseCallResult

    suspend fun updateSpendSplitMode(
        spend: Spend,
        newSplitMode: SplitMode
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateSpendSplitModeFirebaseCallResult.postValue(
                repository.get().updateSpendName(spend, newSplitMode)
            )
        }
    }

    private val _updateSpendAmountFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val updateSpendAmountFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _updateSpendAmountFirebaseCallResult

    suspend fun updateSpendAmount(
        spend: Spend,
        newAmount: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateSpendAmountFirebaseCallResult.postValue(
                repository.get().updateSpendName(spend, newAmount)
            )
        }
    }

    private val _updateSpendGeoPointFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val updateSpendGeoPointFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _updateSpendGeoPointFirebaseCallResult

    suspend fun updateSpendGeoPoint(
        spend: Spend,
        newGeoPoint: GeoPoint
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateSpendGeoPointFirebaseCallResult.postValue(
                repository.get().updateSpendName(spend, newGeoPoint)
            )
        }
    }

    private val _addSpendMemberFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val addSpendMemberFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _addSpendMemberFirebaseCallResult

    suspend fun addSpendMember(
        spend: Spend,
        newMember: Friend
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _addSpendMemberFirebaseCallResult.postValue(
                repository.get().updateSpendName(spend, newMember)
            )
        }
    }

    private val _addSpendMembersFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val addSpendMembersFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _addSpendMembersFirebaseCallResult

    suspend fun addSpendMembers(
        spend: Spend,
        newMembers: List<Friend>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _addSpendMembersFirebaseCallResult.postValue(
                repository.get().updateSpendName(spend, newMembers)
            )
        }
    }

    private val _deleteSpendMemberFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val deleteSpendMemberFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _deleteSpendMemberFirebaseCallResult

    suspend fun deleteSpendMember(
        spend: Spend,
        member: List<Friend>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _deleteSpendMemberFirebaseCallResult.postValue(
                repository.get().updateSpendName(spend, member)
            )
        }
    }

    private val _deleteSpendSpendFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val deleteSpendSpendFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _deleteSpendSpendFirebaseCallResult

    suspend fun deleteSpendSpend(spend: Spend) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateSpendNameFirebaseCallResult.postValue(
                repository.get().deleteSpendSpend(spend)
            )
        }
    }
}