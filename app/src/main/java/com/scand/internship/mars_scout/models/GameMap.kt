package com.scand.internship.mars_scout.models

import android.os.Parcelable
import android.util.Size
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class GameMap(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val size: Size? = null,
    val blocks: MutableList<MutableList<MapBlock>>? = null,
) : Parcelable {
    constructor(name: String) :
            this(UUID.randomUUID(), name, Size(16, 16), mutableListOf())
}

