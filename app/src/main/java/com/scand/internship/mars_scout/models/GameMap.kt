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
            this(UUID.randomUUID(), name, DEFAULT_SIZE, mutableListOf())

    companion object{
        val DEFAULT_SIZE = Size(16,16)
    }
}

data class MapResponse(val id: String = "",
                       val name: String = "",
                       val size: Map<String, Int>? = null,)

fun MapResponse.mapResponseToMapUI(): GameMap {
    var sizeMap: Size? = null

    if(!size.isNullOrEmpty()) {
        val width = size["width"]
        val height = size["height"]

        if (width != null && height != null) {
            sizeMap = Size(width, height)
        }
    }

    return GameMap(UUID.fromString(id), name, sizeMap, null )
}

fun MapResponse.isValid() = id.isNotBlank()
        && name.isNotBlank()

data class MapDB(val id: String = "",
                 val name: String = "",
                 val size: Map<String, Int>? = null,
                 val blocks: MutableList<MutableList<MapBlock>>? = null,)

fun GameMap.mapToMapDB(): MapDB {
    var sizeResponse: Map<String, Int>? = null

    if (size != null) {
        sizeResponse = mapOf("width" to size.width, "height" to size.height)
    }
    return MapDB(id.toString(), name, sizeResponse, blocks)
}

