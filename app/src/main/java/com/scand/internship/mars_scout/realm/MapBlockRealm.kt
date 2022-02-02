package com.scand.internship.mars_scout.realm

import com.scand.internship.mars_scout.models.BlockType
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId
import kotlin.random.Random

open class MapBlockRealm(
    @PrimaryKey
    var id: Int = Random.nextInt(),
    @Required
    var name: String = "",
    var type: BlockType? = null,
    var coordinates: Pair<Int, Int>? = null
): RealmObject()