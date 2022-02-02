package com.scand.internship.mars_scout.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapBlock(
    val id: Int,
    val type: BlockType?,
    val coordinates: Pair<Int, Int>
) : Parcelable {
    constructor(id: Int) :
            this(id, BlockType.GROUND, Pair(0,0))
}