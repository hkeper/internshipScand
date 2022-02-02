package com.scand.internship.mars_scout.repository

import com.scand.internship.mars_scout.models.MapBlock
import kotlinx.coroutines.flow.Flow
import java.util.*

interface GameMapRepository {

    fun addMap(name: String): Flow<GameMapStatus>

    fun getMap(id: UUID): Flow<GameMapStatus>

    fun getMaps(): Flow<GameMapStatus>

    fun updateMap(id: UUID, blocks: MutableList<MutableList<MapBlock>>): Flow<GameMapStatus>

    fun deleteMap(id: UUID): Flow<GameMapStatus>

}