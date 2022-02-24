package com.scand.internship.mars_scout.repository

import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.MapBlock
import com.scand.internship.mars_scout.realm.GameMapDatabaseOperations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

class GameMapRepositoryImpl @Inject constructor(
    private val databaseOperations: GameMapDatabaseOperations
): GameMapRepository {

    override var editedMapID: UUID? = null

    override fun getEditedMapIDFromList(): UUID?{
        val temp = editedMapID
        editedMapID = null
        return temp
    }

    override fun addMap(gameMap: GameMap): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        databaseOperations.insertMap(gameMap)
        emit(GameMapStatus.Added)
    }.flowOn(Dispatchers.IO)

    override fun getMap(id: UUID): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        val gameMap = databaseOperations.retrieveMap(id)
        emit(GameMapStatus.MapRetrieved(gameMap))
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

    override fun clearDB(): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        databaseOperations.clearDB()
        emit(GameMapStatus.Cleared)
    }.flowOn(Dispatchers.IO)

}

