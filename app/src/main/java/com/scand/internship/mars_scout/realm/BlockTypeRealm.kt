package com.scand.internship.mars_scout.realm

import com.scand.internship.mars_scout.models.BlockType
import io.realm.RealmObject

open class BlockTypeRealm: RealmObject() {
    private var strField: String = BlockType.GROUND.name
    var enumField: BlockType
        get() { return BlockType.valueOf(strField) }
        set(newMyEnum) { strField = newMyEnum.name }
}