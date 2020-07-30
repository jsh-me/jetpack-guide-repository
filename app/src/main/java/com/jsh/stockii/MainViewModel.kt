package com.jsh.stockii

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.stream.Collectors

class MainViewModel: ViewModel() {
    private val TAG: String? = MainViewModel::class.java.simpleName
    var itemLiveData = MutableLiveData<List<Store>>()
    var loadingLiveData = MutableLiveData<Boolean>()
    lateinit var location: Location

    private var retrofitService = Retrofit.Builder()
            .baseUrl(Consts.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MaskService::class.java)

    fun fetchStoreInfo() {
        //loading start
        loadingLiveData.value = true

        viewModelScope.launch{
           val storeInfo =  retrofitService
               .fetchStoreInfo(location.latitude, location.longitude)
               .stores

            itemLiveData.value = storeInfo.stream()
                .filter{ it.remain_stat != null }
                .filter{ it.remain_stat != "empty"}
                .collect(Collectors.toList())
        }
        //loading finish
        loadingLiveData.value = false
    }
}