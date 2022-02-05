package com.scand.internship.mars_scout.realm

import com.scand.internship.mars_scout.models.BlockType
import io.realm.RealmObject

open class BlockTypeRealm: RealmObject() {
    private var strField: String = BlockType.GROUND.name

    // Public field exposing setting/getting the enum
    var enumField: BlockType?
        get() = BlockType.values().first { it.name == strField }
        set(value) {
            if (value != null) {
                strField = value.name
            }
        }
}