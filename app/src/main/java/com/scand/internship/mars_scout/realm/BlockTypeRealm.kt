package com.scand.internship.mars_scout.realm

import com.scand.internship.mars_scout.models.BlockType
import io.realm.RealmObject

open class BlockTypeRealm: RealmObject() {
    // Custom private backing field representing the enum
    private var strField: String = BlockType.GROUND.name

    // Public field exposing setting/getting the enum
    var enumField: BlockType
        get() = BlockType.values().first { it.name == strField }
        set(value) {
            strField = value.name
        }
}