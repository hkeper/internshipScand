package com.scand.internship.mars_scout.realm

import com.scand.internship.mars_scout.models.MapBlock
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class GameMapRealm(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    @Required
    var name: String = "",
    var size: String? = null,
    var blocks: RealmList<MapBlockRealm>? = null
): RealmObject()

