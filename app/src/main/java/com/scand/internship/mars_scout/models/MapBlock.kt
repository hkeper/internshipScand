package com.scand.internship.mars_scout.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapBlock(
    val id: String,
    val name: String,
    val type: BlockType = BlockType.GROUND,
    val coordinates: String? = null
) : Parcelable