package com.scand.internship.mars_scout.firebase

import com.scand.internship.mars_scout.models.GameMap
import java.util.*

interface FirebaseDatabaseInterface {

    fun addMap(map: GameMap)

    fun getMap(id: UUID, onResult: (GameMap) -> Unit)

    fun deleteMap(id: UUID)

    suspend fun getMapsList(onResult: (List<GameMap>) -> Unit)

    fun clearDB()

}