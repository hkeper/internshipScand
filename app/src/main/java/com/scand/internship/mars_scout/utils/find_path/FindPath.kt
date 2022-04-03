package com.scand.internship.mars_scout.utils.find_path

import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.GameMap


fun findPath(map: GameMap):  MutableList<Int>{
    val d: MutableList<Int> = mutableListOf() //paths to every block
    val v: MutableList<Int> = mutableListOf() //visited blocks
    val blocksWeightList: MutableList<MutableList<Int>> = mutableListOf() //list of connections
    val start = setStartPointUtil(map) //start block
    var end = setEndPointUtil(map) - 1 // finish block - 1
    var temp: Int; var minindex: Int; var min: Int
    val pointsWeight = setPointsWeight(map) //all points with there weight
    var path: MutableList<Int> = mutableListOf() //ids of blocks with shortest path
    val shortestPathPoints: MutableList<Int> = mutableListOf()  //list of points within shortest path

    if(map.size != null && !pointsWeight.isNullOrEmpty()) {
        val mapWidth = map.size.width
        val mapHeight = map.size.height
        val size = mapWidth * mapHeight

        for (i in 1..size) {
            val blocksWeightLine = mutableListOf<Int>()
            for (j in 1..size) {
                val lastInLine = i % mapWidth         //check if element the last in the line of map blocks
                val firstInLine = i % (mapWidth + 1)  //check if element the first in the line

                if (j==(i + 1) && lastInLine!=0) {
                    pointsWeight[j]?.let {
                        blocksWeightLine.add(it[0])
                    }
                } else if(j==(i - 1) && firstInLine!=0) {
                    pointsWeight[j]?.let {
                        blocksWeightLine.add(it[0])
                    }
                }
                else if(j==i + mapWidth) {
                    pointsWeight[j]?.let {
                        blocksWeightLine.add(it[0])
                    }
                }
                else if(j==i - mapWidth) {
                    pointsWeight[j]?.let {
                        blocksWeightLine.add(it[0])
                    }
                } else {
                    blocksWeightLine.add(0)
                }
            }
            blocksWeightList.add(blocksWeightLine)
        }

        for (i in 0 until size) {
            d.add(10000)
            v.add(1)
        }
        d[start] = 0

        do {
            minindex = 10000
            min = 10000

            for (i in 0 until size)
            {
                if ((v[i] == 1) && (d[i]<min))
                {
                    min = d[i]
                    minindex = i
                }
            }
            if (minindex != 10000)
            {
                for (i in 0 until size)
                {
                    if (blocksWeightList[minindex][i] > 0)
                    {
                        temp = min + blocksWeightList[minindex][i]
                        if (temp < d[i])
                        {
                            d[i] = temp
                        }
                    }
                }
                v[minindex] = 0
            }

        } while (minindex < 10000)

        shortestPathPoints.add(end + 1)
        var k = 1
        var weight = d[end]

        while (end != start)
        {
            for (i in 0 until size)
                if (blocksWeightList[i][end] != 0)
                {
                    temp = weight - blocksWeightList[i][end]
                    if (temp == d[i])
                    {
                        weight = temp
                        end = i
                        shortestPathPoints.add(i + 1)
                        k++
                    }
                }
        }

        var sum = 0 // sum of blocks weight, must be < 1000

        for ( i in k - 1 downTo 0){
            pointsWeight[shortestPathPoints[i]]?.let {
                sum += it[0]
                path.add( it[1] )
            }
        }
        if (sum >= 1000){
            path = mutableListOf()
        }
    }
    return path
}

fun setPointsWeight(map: GameMap): Map<Int, MutableList<Int>>{
    val pointsWeight = mutableMapOf<Int, MutableList<Int>>()
    val mapBlocks = map.blocks

    if(!mapBlocks.isNullOrEmpty()) {
        var point = 1
        for (y in 0 until mapBlocks.size) {
            for (x in 0 until mapBlocks[y].size) {
                val b = mapBlocks[y][x]
                if (b.type != null) {
                    pointsWeight[point++] = mutableListOf ( getBlockWeightAccordingToType(b.type),
                    b.id )
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
        BlockType.PIT -> 1000
        BlockType.GROUND -> 1
        else -> 0
    }
}

fun setStartPointUtil(map: GameMap): Int {
    return (map.size?.width?.div(2)) ?: 0
}

fun setEndPointUtil(map: GameMap): Int {
    val mapBlocks = map.blocks
    var end = 0

    if(!mapBlocks.isNullOrEmpty()) {
        val start = (map.size?.width?.div(2)) ?: 0
        var point = 1
        for (y in 0 until mapBlocks.size) {
            for (x in 0 until mapBlocks[y].size) {
                if (y == map.size?.height?.minus(1) &&
                        x == start) {
                    end = point
                }
                point++
            }
        }
    }
    return end
}

