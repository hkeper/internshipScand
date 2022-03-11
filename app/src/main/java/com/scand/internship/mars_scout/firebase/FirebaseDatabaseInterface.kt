package com.scand.internship.mars_scout.firebase

import com.scand.internship.mars_scout.models.GameMap

interface FirebaseDatabaseInterface {

    fun addMap(map: GameMap)

}