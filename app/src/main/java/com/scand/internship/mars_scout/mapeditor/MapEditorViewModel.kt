package com.scand.internship.mars_scout.mapeditor

import android.util.Size
import androidx.lifecycle.*
import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.MapBlock
import com.scand.internship.mars_scout.repository.GameMapRepository
import com.scand.internship.mars_scout.repository.GameMapStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random


class MapEditorViewModel @Inject constructor(
    private val mapsRepository: GameMapRepository
) : ViewModel() {

    private val _gameMapStatus = MutableLiveData<GameMapStatus?>()
    val gameMapStatus: LiveData<GameMapStatus?>
        get() {
            return _gameMapStatus
        }

    private val _gameMap = MutableLiveData<GameMap>()
    val gameMap: LiveData<GameMap> = _gameMap

    private val _isEditMode = MutableLiveData<Boolean>()
    val isEditMode: LiveData<Boolean> = _isEditMode

    private val _typeChosenMapBlock = MutableLiveData<BlockType?>()
    val typeChosenMapBlock: LiveData<BlockType?> = _typeChosenMapBlock

    private val _isBlockChosen: MutableLiveData<Boolean> = MutableLiveData()
    val isBlockChosen: LiveData<Boolean>
        get() = _isBlockChosen

    init{
        val id = UUID.randomUUID()
        _isEditMode.value = false
        _gameMap.value = GameMap(
            id, id.toString(), GameMap.DEFAULT_SIZE, null)
        _gameMapStatus.value = null
        _typeChosenMapBlock.value = null
        _isBlockChosen.value = false
    }

    fun getEditedMapByID() {
        mapsRepository.getEditedMapIDFromList()?.let { id ->
            _isEditMode.value = true
            viewModelScope.launch {
                mapsRepository.getMap(id).collect {
                    _gameMapStatus.value = it
                }
            }
        }
    }

    fun generateMap(mapSize: Size) {
        viewModelScope.launch {
            val blocks: MutableList<MutableList<MapBlock>> = mutableListOf()
            var type: BlockType
            val id = UUID.randomUUID()

            for (y in 0 until mapSize.height) {
                val blocksLine = mutableListOf<MapBlock>()
                for (x in 0 until mapSize.width) {
                    type = when (Random.nextInt(0, 4)) {
                        0 -> BlockType.GROUND
                        1 -> BlockType.HILL
                        2 -> BlockType.PIT
                        else -> BlockType.SAND
                    }
                    blocksLine.add(MapBlock(Random.nextInt(), type, mutableListOf(x,y)))
                }
                blocks.add(blocksLine)
            }
            if(_isEditMode.value == true) {
                _gameMap.value = GameMap(_gameMap.value?.id ?: id,
                    _gameMap.value?.name ?: id.toString(),
                    _gameMap.value?.size, blocks)
            }else{
                _gameMap.value = GameMap(id, id.toString(), mapSize, blocks)
            }
        }
    }

    fun onBlockChosen(type: BlockType) {
        _typeChosenMapBlock.value = type
        _isBlockChosen.value = true
    }

    fun onBlockNotChosen() {
        _typeChosenMapBlock.value = null
        _isBlockChosen.value = false
    }

    fun clearMap(){
        _gameMap.value = GameMap(_gameMap.value?.id ?: UUID.randomUUID(),
            _gameMap.value?.name ?: UUID.randomUUID().toString(),
            _gameMap.value?.size, mutableListOf())
    }

    fun putUIMapToModelMap(map: GameMap){
        _gameMap.value = map
    }

    fun setMapName(name: String){
        _gameMap.value = GameMap(_gameMap.value?.id ?: UUID.randomUUID(), name,
            _gameMap.value?.size, _gameMap.value?.blocks ?: mutableListOf())
    }

    fun saveUIMapToModelMap(){
        _gameMap.value?.let { map ->
            viewModelScope.launch {
                mapsRepository.addMap(map).collect {
                    _gameMapStatus.value = it
                }
            }
        }
    }

    fun setEditMode(value: Boolean){
        _isEditMode.value = value
    }

}

