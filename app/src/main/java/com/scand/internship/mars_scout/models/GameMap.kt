package com.scand.internship.mars_scout.models

import android.os.Parcelable
import android.util.Size
import com.scand.internship.mars_scout.models.MapBlock
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameMap(
    val id: Int,
    val name: String,
    val size: Size,
    val blocks: List<MapBlock>?
) : Parcelable {
    constructor(id: Int, name: String, blocks: List<MapBlock>?) :
            this(id, name, Size(16, 16), blocks)
}