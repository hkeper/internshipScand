package com.scand.internship.mars_scout.repository

import com.scand.internship.mars_scout.firebase.FirebaseDatabaseInterface
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
    private val databaseOperations: GameMapDatabaseOperations,
    private val firebaseDB: FirebaseDatabaseInterface
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
        firebaseDB.addMap(gameMap)
        emit(GameMapStatus.Added)
    }.flowOn(Dispatchers.IO)

    override fun getMap(id: UUID): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        val gameMap = databaseOperations.retrieveMap(id)
        emit(GameMapStatus.MapRetrieved(gameMap))
    }.flowOn(Dispatchers.IO)

    // Make sure that all maps from local db appears on the screen and in the firebase realtime
    override fun getMaps(): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        var mapsNet: MutableList<GameMap> = mutableListOf()
        firebaseDB.getMapsList(){
            mapsNet = it.toMutableList()
        }
        var mapsDB = databaseOperations.retrieveMaps().toMutableList()
        kotlinx.coroutines.delay(1000)
        if(mapsDB.isNullOrEmpty()){
            mapsDB.addAll(mapsNet)
        }
        if(mapsNet.isNullOrEmpty()){
            mapsNet.addAll(mapsDB)
        }
        for (DBItem in mapsDB){
            for (netItem in mapsNet) {
                if (DBItem.id != netItem.id) {
                    databaseOperations.insertMap(netItem)
                }
            }
        }
        kotlinx.coroutines.delay(1000)
        mapsDB = databaseOperations.retrieveMaps().toMutableList()
        for (DBItem in mapsDB){
            firebaseDB.addMap(DBItem)
        }
        emit(GameMapStatus.MapsRetrieved(mapsDB))
    }.flowOn(Dispatchers.IO)

    override fun updateMapBlocks(
        id: UUID,
        blocks: MutableList<MutableList<MapBlock>>
    ): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        databaseOperations.updateMapBlocks(id, blocks)
        val gameMap = databaseOperations.retrieveMap(id)
        if (gameMap != null) {
            firebaseDB.addMap(gameMap)
        }
        emit(GameMapStatus.BlocksAdded)
    }.flowOn(Dispatchers.IO)

    override fun deleteMap(id: UUID): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        databaseOperations.removeMap(id)
        firebaseDB.deleteMap(id)
        emit(GameMapStatus.Deleted)
    }.flowOn(Dispatchers.IO)

    override fun clearDB(): Flow<GameMapStatus> = flow {
        emit(GameMapStatus.Loading)
        databaseOperations.clearDB()
        firebaseDB.clearDB()
        emit(GameMapStatus.Cleared)
    }.flowOn(Dispatchers.IO)

}

