package com.scand.internship.mars_scout.mapeditor.model

import android.os.Parcelable
import android.util.Size
import com.scand.internship.mars_scout.models.MapBlock
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameMap(
    val id: String,
    val name: String,
    val size: Size,
    val blocks: List<MapBlock>?
) : Parcelable