package com.scand.internship.mars_scout.realm

import com.scand.internship.mars_scout.models.BlockType
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import kotlin.random.Random

open class MapBlockRealm(
    @PrimaryKey
    var id: Int = Random.nextInt(),
    var type: BlockTypeRealm? = null,
    var coordinates: RealmList<Int>? = null
): RealmObject()