package com.scand.internship.mars_scout.maplist

import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.scand.internship.mars_scout.models.GameMap
import java.util.*

class MapListViewModel : ViewModel() {

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _maps = MutableLiveData<List<GameMap>>()
    val maps: LiveData<List<GameMap>> = _maps


    init{
        _maps.value = listOf(GameMap("test", mutableListOf()), GameMap("test1", mutableListOf()))
        _dataLoading.value = false
    }

    fun setLoadingToFalse(){
        _dataLoading.value = false
    }

}