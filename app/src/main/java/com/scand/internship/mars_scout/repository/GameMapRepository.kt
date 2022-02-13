package com.scand.internship.mars_scout.repository

import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.MapBlock
import kotlinx.coroutines.flow.Flow
import java.util.*

interface GameMapRepository {

    fun addMap(name: String): Flow<GameMapStatus>

    suspend fun getMap(id: UUID): GameMap?

    fun getMaps(): Flow<GameMapStatus>

    fun updateMapBlocks(id: UUID, blocks: MutableList<MutableList<MapBlock>>): Flow<GameMapStatus>

    fun deleteMap(id: UUID): Flow<GameMapStatus>

    suspend fun insertTransferredMapID(id: UUID)

    suspend fun getTransferredMapID(): UUID?

}