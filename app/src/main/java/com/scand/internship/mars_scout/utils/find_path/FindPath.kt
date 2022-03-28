package com.scand.internship.mars_scout.utils.find_path

import android.graphics.drawable.Drawable
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.MapBlock
import kotlin.random.Random


public fun findPath(map: GameMap): List<MapBlock>{
    val pathBlocks: List<MapBlock> = mutableListOf()
    var visitedBlocks: MutableList<Int> = mutableListOf()
    val blocksWeightList: MutableList<MutableList<Int>> = mutableListOf()
    var begin_index = 0; var temp = 0; var minindex = 0; var min = 0
    val pointsWeight = setPointsWeight(map)

    if(map.size != null) {

        for (i in 0..(map.size.height * map.size.height)) {
            for (j in 0..(map.size.width * map.size.width)) {
!!!!
                when (j) {
                    i -> blocksWeightList[i][j] = 0
                    i+1 -> pointsWeight[j]?.let { blocksWeightList[i][j] = it }
                    i-1 -> pointsWeight[j]?.let { blocksWeightList[i][j] = it }

                }

//                if(i==j) {
//                    blocksWeightList[i][j] = 0
//                } else if(j==(i+1)) {
//                    blocksWeightList[i][j] = pointsWeight[j]!!
//                }
            }
        }

    }

    return pathBlocks
}

fun setPointsWeight(map: GameMap): Map<Int, Int>{
    val pointsWeight = mutableMapOf<Int, Int>()
    val mapBlocks = map.blocks

    if(!mapBlocks.isNullOrEmpty()) {
        var point = 0
        for (y in 0 until mapBlocks.size) {
            for (x in 0 until mapBlocks[y].size) {
                val b = mapBlocks[y][x]
                if (b.type != null) {
                    pointsWeight[point++] = getBlockWeightAccordingToType(b.type)
                }
            }
        }

    }

    return pointsWeight
}

fun getBlockWeightAccordingToType(type: BlockType?): Int{
    return when (type) {
        BlockType.SAND -> 3
        BlockType.HILL -> 2
        BlockType.PIT -> 100
        BlockType.GROUND -> 1
        else -> 0
    }
}

fun setStartPointUtil(map: GameMap): Int {
    return (map.size?.width?.div(2)) ?: 0
}