package com.scand.internship.mars_scout.maplist

import android.util.Size
import androidx.lifecycle.*
import com.scand.internship.mars_scout.firebase.FirebaseDatabaseInterface
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.MapBlock
import com.scand.internship.mars_scout.repository.GameMapRepository
import com.scand.internship.mars_scout.repository.GameMapStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class MapListViewModel @Inject constructor(
    private val mapsRepository: GameMapRepository
) : ViewModel() {

    private val _gameMapStatus = MutableLiveData<GameMapStatus>()
    val gameMapStatus: LiveData<GameMapStatus>
        get() {
            return _gameMapStatus
        }

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _maps = MutableLiveData<MutableList<GameMap>>()
    val maps: LiveData<MutableList<GameMap>> = _maps

    init{
//        clearDB()
        _gameMapStatus.value = GameMapStatus.Loading
        getMapsListFromDB()
        _dataLoading.value = false
    }

    private fun clearDB(){
        viewModelScope.launch {
            mapsRepository.clearDB().collect {
                _gameMapStatus.value = it
            }
        }
    }

    fun getMapsListFromDB(){
        viewModelScope.launch {
            mapsRepository.getMaps().collect {
                _gameMapStatus.value = it
            }
        }
    }

    fun putMapsToViewModelList(DBmaps: MutableList<GameMap>){
        _maps.value = DBmaps
    }

    fun setLoadingToFalse(){
        getMapsListFromDB()
        _dataLoading.value = false
    }

    fun removeMapAtPosition(position: Int, mapId: UUID){
        viewModelScope.launch {
            mapsRepository.deleteMap(mapId) .collect {
                _gameMapStatus.value = it
            }
        }
        _maps.value?.removeAt(position)
    }

    fun setEditedMapID(id: UUID?) {
        mapsRepository.editedMapID = id
    }

}