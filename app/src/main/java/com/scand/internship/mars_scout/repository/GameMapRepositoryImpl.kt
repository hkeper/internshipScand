package com.scand.internship.mars_scout.repository

import com.scand.internship.mars_scout.models.MapBlock
import com.scand.internship.mars_scout.realm.GameMapDatabaseOperations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*

class GameMapRepositoryImpl(
    private val databaseOperations: GameMapDatabaseOperations
): GameMapRepository {

    override fun addMap(name: String): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        databaseOperations.insertMap(name)
        emit(GameMapStatus.Added)
    }.flowOn(Dispatchers.IO)

    override fun getMap(id: UUID): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        val map = databaseOperations.retrieveMap(id)
        emit(GameMapStatus.MapRetrieved(map))
    }.flowOn(Dispatchers.IO)

    override fun getMaps(): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        val maps = databaseOperations.retrieveMaps()
        emit(GameMapStatus.MapsRetrieved(maps))
    }.flowOn(Dispatchers.IO)

    override fun updateMapBlocks(
        id: UUID,
        blocks: MutableList<MutableList<MapBlock>>
    ): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        databaseOperations.updateMapBlocks(id, blocks)
        emit(GameMapStatus.BlocksAdded)
    }.flowOn(Dispatchers.IO)

    override fun deleteMap(id: UUID): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        databaseOperations.removeMap(id)
        emit(GameMapStatus.Deleted)
    }.flowOn(Dispatchers.IO)
}

