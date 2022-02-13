package com.scand.internship.mars_scout.realm

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class TransferredGameMapIDRealm(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var mapId: UUID? = null
): RealmObject()