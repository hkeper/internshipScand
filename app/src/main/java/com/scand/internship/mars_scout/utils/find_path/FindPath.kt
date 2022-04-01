package com.scand.internship.mars_scout.utils.find_path

import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.GameMap


fun findPath(map: GameMap):  MutableList<Int>{
    val d: MutableList<Int> = mutableListOf() //min path
    val v: MutableList<Int> = mutableListOf() //visited blocks
    val blocksWeightList: MutableList<MutableList<Int>> = mutableListOf() //list of connections
    val begin_index = setStartPointUtil(map)
    var end = setEndPointUtil(map) - 1 // индекс конечной вершины - 1
    var temp = 0; var minindex = 0; var min = 0
    val pointsWeight = setPointsWeight(map)
    val path: MutableList<Int> = mutableListOf()
    val ver: MutableList<Int> = mutableListOf()  // массив посещенных вершин

    if(map.size != null) {
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
        d[begin_index] = 0

        do {
            minindex = 10000
            min = 10000

            for (i in 0 until size)
            { // Если вершину ещё не обошли и вес меньше min
                if ((v[i] == 1) && (d[i]<min))
                { // Переприсваиваем значения
                    min = d[i]
                    minindex = i
                }
            }
            // Добавляем найденный минимальный вес
            // к текущему весу вершины
            // и сравниваем с текущим минимальным весом вершины
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

        ver.add(end + 1)  // начальный элемент - конечная вершина
        var k = 1 // индекс предыдущей вершины
        var weight = d[end]; // вес конечной вершины

        while (end != begin_index) // пока не дошли до начальной вершины
        {
            for (i in 0 until size) // просматриваем все вершины
                if (blocksWeightList[i][end] != 0)   // если связь есть
                {
                    temp = weight - blocksWeightList[i][end]; // определяем вес пути из предыдущей вершины
                    if (temp == d[i]) // если вес совпал с рассчитанным
                    {                 // значит из этой вершины и был переход
                        weight = temp // сохраняем новый вес
                        end = i       // сохраняем предыдущую вершину
                        ver.add(i + 1) // и записываем ее в массив
                        k++
                    }
                }
        }

        for ( i in k - 1 downTo 0){
            pointsWeight[ver[i]]?.let {
                path.add( it[1] )
            }
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
        BlockType.PIT -> 100
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

