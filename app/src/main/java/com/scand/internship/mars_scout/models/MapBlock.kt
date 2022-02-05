package com.scand.internship.mars_scout.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class MapBlock(
    val id: Int = Random.nextInt(),
    val type: BlockType? = null,
    val coordinates: MutableList<Int>? = null,
) : Parcelable