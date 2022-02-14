package com.scand.internship.mars_scout.mapeditor

import android.util.Size
import androidx.lifecycle.*
import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.MapBlock
import com.scand.internship.mars_scout.repository.GameMapRepository
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random


class MapEditorViewModel @Inject constructor(
    private val mapsRepository: GameMapRepository
//    private val state: SavedStateHandle
) : ViewModel() {

//    @AssistedFactory
//    interface Factory : ViewModelAssistedFactory<MapEditorViewModel>

    private val _mapGenerating = MutableLiveData(false)
    val mapGenerating: LiveData<Boolean> = _mapGenerating

    private val _gameMap = MutableLiveData<GameMap>()
    val gameMap: LiveData<GameMap> = _gameMap

    private val _typeChosenMapBlock = MutableLiveData<BlockType?>()
    val typeChosenMapBlock: LiveData<BlockType?> = _typeChosenMapBlock

    private val _isBlockChosen: MutableLiveData<Boolean> = MutableLiveData()
    val isBlockChosen: LiveData<Boolean>
        get() = _isBlockChosen

    init{
        getEditedMapByID()

//        _gameMap.value = state.get<GameMap>("map")

        _typeChosenMapBlock.value = null
        _isBlockChosen.value = false
    }

    private fun getEditedMapByID(){
        val l: UUID? = mapsRepository.editedMapID
        viewModelScope.launch {
//            l = mapsRepository.getTransferredMapID()!!
//            val o = _gameMap.value?.id
            _gameMap.value = l?.let { mapsRepository.getMap(it) }
        }
        val t = _gameMap.value
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
                    blocksLine.add(MapBlock(("" + x + y).toInt(), type, mutableListOf(x,y)))
                }
                blocks.add(blocksLine)
            }
            _gameMap.value = GameMap(id, id.toString(), mapSize, blocks)
        }
    }

    fun addMap(name: String){
        viewModelScope.launch {

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

    fun saveMap(map: GameMap){
        _gameMap.value = map
    }

    fun setMapName(name: String){
        _gameMap.value = GameMap(_gameMap.value?.id ?: UUID.randomUUID(), name,
            _gameMap.value?.size, _gameMap.value?.blocks ?: mutableListOf())
    }

}

