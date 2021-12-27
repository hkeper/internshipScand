package com.scand.internship.mars_scout.mapeditor

import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.MapBlock
import kotlin.random.Random

class MapEditorViewModel : ViewModel() {

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
        _gameMap.value = GameMap(1,"1", Size(0,0), null)
        _typeChosenMapBlock.value = null
        _isBlockChosen.value = false
    }

    fun generateMap(mapSize: Size): GameMap? {
        val blocks = mutableListOf<MapBlock>()
        var type : BlockType

        for (x in 0 until mapSize.width){
            for (y in 0 until mapSize.height){
                type = when(Random.nextInt(0,4)){
                    0 -> BlockType.GROUND
                    1 -> BlockType.HILL
                    2 -> BlockType.PIT
                    else -> BlockType.SAND
                }
                blocks.add(MapBlock(("" + x + y).toInt(), ("" + x + y), type, Pair(x,y)))
            }
        }
        _gameMap.value = GameMap(1,"1", mapSize, blocks)
        return _gameMap.value
    }

    fun onBlockChosen(type: BlockType) {
        _typeChosenMapBlock.value = type
        _isBlockChosen.value = true
    }

    fun onBlockNotChosen() {
        _typeChosenMapBlock.value = null
        _isBlockChosen.value = false
    }

}