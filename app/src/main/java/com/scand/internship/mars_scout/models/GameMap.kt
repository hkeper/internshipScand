package com.scand.internship.mars_scout.models

import android.os.Parcelable
import android.util.Size
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class GameMap constructor(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val size: Size? = null,
    val blocks: MutableList<MutableList<MapBlock>>? = null,
) : Parcelable {
    constructor(name: String) :
            this(UUID.randomUUID(), name, Size(16, 16), mutableListOf())

    companion object{
        val DEFAULT_SIZE = Size(16,16)
    }
}

data class MapResponse(val id: String = "",
                       val name: String = "",
                       val size: Size? = null,
                       val blocks: MutableList<MutableList<MapBlockDB>>? = null)

fun MapResponse.isValid() = id.isNotBlank()
        && name.isNotBlank()

fun MapResponse.mapToUIMap() = GameMap(
    UUID.fromString(id), name, size, blocks?.let { mapDBBlocksToUI(it) }
)

fun GameMap.mapToResponseMap() = MapResponse(
        id.toString(), name, size, blocks?.let { mapUIBlocksToDB(it) }
    )

private fun mapUIBlocksToDB(blocks: MutableList<MutableList<MapBlock>>): MutableList<MutableList<MapBlockDB>>{

    val mappedBlocks: MutableList<MutableList<MapBlockDB>> = mutableListOf()

    for (y in 0 until blocks.size) {
        val blocksLine = mutableListOf<MapBlockDB>()
        for (x in 0 until blocks[y].size) {
            blocksLine.add(blocks[y][x].mapToDBBlock())
        }
        mappedBlocks.add(blocksLine)
    }
    return mappedBlocks
}

private fun mapDBBlocksToUI(blocks: MutableList<MutableList<MapBlockDB>>): MutableList<MutableList<MapBlock>>{

    val mappedBlocks: MutableList<MutableList<MapBlock>> = mutableListOf()

    for (y in 0 until blocks.size) {
        val blocksLine = mutableListOf<MapBlock>()
        for (x in 0 until blocks[y].size) {
            blocksLine.add(blocks[y][x].mapToUIBlock())
        }
        mappedBlocks.add(blocksLine)
    }
    return mappedBlocks
}
