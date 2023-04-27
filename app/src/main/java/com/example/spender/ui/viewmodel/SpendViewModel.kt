package com.example.spender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.DataResult
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.spend.Spend
import com.example.spender.domain.model.spend.SpendMember
import com.example.spender.domain.repository.SpendRepository
import com.example.spender.domain.usecases.interfaces.SpendUpdateUseCase
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class SpendViewModel @Inject constructor(
    private val repository: dagger.Lazy<SpendRepository>,
    private val spendUpdateUseCase: dagger.Lazy<SpendUpdateUseCase>
) : ViewModel() {

    /*
     * Create spend
     */

    private val _createSpendDataResult = MutableLiveData<DataResult<String>>()
    val createSpendDataResult: LiveData<DataResult<String>> =
        _createSpendDataResult
    private val _createSpendMsgShow = MutableLiveData<Boolean>()
    val createSpendMsgShow: LiveData<Boolean> = _createSpendMsgShow

    fun createSpend(
        trip: Trip,
        name: String,
        category: String,
        splitMode: Int,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _createSpendDataResult.postValue(
                repository.get().createSpend(
                    trip,
                    name,
                    category,
                    splitMode,
                    amount,
                    geoPoint,
                    members
                )
            )
        }.invokeOnCompletion {
            _createSpendMsgShow.postValue(true)
        }
    }

    fun doNotShowCreateSpendMsg() {
        _createSpendMsgShow.postValue(false)
    }

    /*
     * Update spend
     */

    private val _updateSpendDataResult = MutableLiveData<DataResult<String>>()
    val updateSpendDataResult: LiveData<DataResult<String>> = _updateSpendDataResult
    private val _updateSpendMsgShow = MutableLiveData<Boolean>()
    val updateSpendMsgShow: LiveData<Boolean> = _updateSpendMsgShow

    fun updateSpend(
        oldSpend: Spend,
        newSpend: Spend
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateSpendDataResult.postValue(
                spendUpdateUseCase.get().invoke(oldSpend, newSpend)
            )
        }.invokeOnCompletion {
            _updateSpendMsgShow.postValue(true)
        }
    }

    fun doNotShowUpdateSpendMsg() {
        _updateSpendMsgShow.postValue(false)
    }
}
