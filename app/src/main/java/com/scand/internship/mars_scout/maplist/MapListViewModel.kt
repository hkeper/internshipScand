package com.scand.internship.mars_scout.maplist

import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.MapBlock
import java.util.*

class MapListViewModel : ViewModel() {

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _maps = MutableLiveData<MutableList<GameMap>>()
    val maps: LiveData<MutableList<GameMap>> = _maps


    init{
        _maps.value = mutableListOf(GameMap("test1"),
            GameMap(UUID.randomUUID(),"test2", Size(16,16), mutableListOf(
                mutableListOf(MapBlock(0, BlockType.GROUND, Pair(0,0)),
                    MapBlock(1, BlockType.SAND, Pair(5,3))),
            )))
        _dataLoading.value = false
    }

    fun setLoadingToFalse(){
        _dataLoading.value = false
    }

    fun removeMapAtPosition(position: Int){
        _maps.value?.removeAt(position)
    }

}