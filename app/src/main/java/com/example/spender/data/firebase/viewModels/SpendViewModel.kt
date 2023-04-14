package com.example.spender.data.firebase.viewModels

import androidx.lifecycle.ViewModel
import com.example.spender.data.firebase.repositoryInterfaces.SpendRepositoryInterface
import javax.inject.Inject

class SpendViewModel@Inject constructor(
    private val repository: dagger.Lazy<SpendRepositoryInterface>
) : ViewModel() {

}