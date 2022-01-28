package com.scand.internship.mars_scout.models

import android.os.Parcelable
import android.util.Size
import io.realm.RealmObject
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class GameMap(
    val id: UUID,
    val name: String?,
    val size: Size?,
    val blocks: MutableList<MutableList<MapBlock>>?
) : Parcelable, RealmObject() {
    constructor(name: String, blocks: MutableList<MutableList<MapBlock>>?) :
            this(UUID.randomUUID(), name, Size(16, 16), blocks)
}