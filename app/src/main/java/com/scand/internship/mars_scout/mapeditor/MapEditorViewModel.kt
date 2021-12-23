package com.scand.internship.mars_scout.mapeditor

import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scand.internship.mars_scout.mapeditor.model.GameMap
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.MapBlock
import kotlin.random.Random

class MapEditorViewModel : ViewModel() {

    private val _mapGenerating = MutableLiveData(false)
    val mapGenerating: LiveData<Boolean> = _mapGenerating

    private val _gameMap = MutableLiveData<GameMap>()
    val gameMap: LiveData<GameMap> = _gameMap

    init{
        _gameMap.value = GameMap("1","1", Size(0,0), null)
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
                blocks.add(MapBlock(("" + x + y), ("" + x + y), type, Pair(x,y)))
            }
        }
        _gameMap.value = GameMap("1","1", mapSize, blocks)
        return _gameMap.value

    }

}