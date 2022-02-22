package com.scand.internship.mars_scout.maplist

import android.util.Size
import androidx.lifecycle.*
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.MapBlock
import com.scand.internship.mars_scout.repository.GameMapRepository
import com.scand.internship.mars_scout.repository.GameMapStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MapListViewModel @Inject constructor(
    private val mapsRepository: GameMapRepository
) : ViewModel() {

    private val _gameMapStatus = MutableLiveData<GameMapStatus>()
    val gameMapStatus: LiveData<GameMapStatus>
        get() {
            return _gameMapStatus
        }

    private val testMap1 = GameMap("test1")
    private val testMap2 = GameMap(UUID.randomUUID(),"test2", Size(16,16), mutableListOf(
        mutableListOf(MapBlock(0, BlockType.GROUND, mutableListOf(0,0)),
            MapBlock(1, BlockType.SAND, mutableListOf(5,3))),
    ))

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _maps = MutableLiveData<MutableList<GameMap>>()
    val maps: LiveData<MutableList<GameMap>> = _maps

    init{
        clearDB()
        setDemoData(testMap1)
        setDemoData(testMap2)
        _maps.value = mutableListOf(testMap1, testMap2)
        _dataLoading.value = false
    }

    private fun setDemoData(map: GameMap){
        viewModelScope.launch {
            mapsRepository.addMap(map).collect{
                _gameMapStatus.value = it
            }
        }
    }

    private fun clearDB(){
        viewModelScope.launch {
            mapsRepository.clearDB().collect {
                _gameMapStatus.value = it
            }
        }
    }

    fun setLoadingToFalse(){
        _dataLoading.value = false
    }

    fun removeMapAtPosition(position: Int){
        _maps.value?.removeAt(position)
    }

    fun setEditedMapID(id: UUID?) {
        mapsRepository.editedMapID = id
    }

}