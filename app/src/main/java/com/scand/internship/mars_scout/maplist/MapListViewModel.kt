package com.scand.internship.mars_scout.maplist

import android.util.Size
import androidx.lifecycle.*
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.MapBlock
import com.scand.internship.mars_scout.repository.GameMapRepository
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MapListViewModel @Inject constructor(
    private val mapsRepository: GameMapRepository
) : ViewModel() {

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _maps = MutableLiveData<MutableList<GameMap>>()
    val maps: LiveData<MutableList<GameMap>> = _maps

    init{
        val l = mapsRepository.getMaps()
        _maps.value = mutableListOf(GameMap("test1"),
            GameMap(UUID.randomUUID(),"test2", Size(16,16), mutableListOf(
                mutableListOf(MapBlock(0, BlockType.GROUND, mutableListOf(0,0)),
                    MapBlock(1, BlockType.SAND, mutableListOf(5,3))),
            )))
        _dataLoading.value = false
    }

    fun setLoadingToFalse(){
        _dataLoading.value = false
    }

    fun removeMapAtPosition(position: Int){
        _maps.value?.removeAt(position)
    }

    fun setEditedMapID(id: UUID) {
        viewModelScope.launch {
            mapsRepository.insertTransferredMapID(id)
        }
    }

}