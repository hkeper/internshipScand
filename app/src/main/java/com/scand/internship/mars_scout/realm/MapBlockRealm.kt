package com.scand.internship.mars_scout.realm

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlin.random.Random

open class MapBlockRealm(
    @PrimaryKey
    var id: Int = Random.nextInt(),
    var type: BlockTypeRealm? = null,
    var coordinates: RealmList<Int>? = null
): RealmObject()