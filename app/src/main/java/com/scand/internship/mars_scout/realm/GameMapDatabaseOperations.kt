package com.scand.internship.mars_scout.realm

import android.util.Size
import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.models.MapBlock
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.Sort
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import java.util.*

class GameMapDatabaseOperations(private val config: RealmConfiguration){

    suspend fun insertMap(name: String) {
        val realm = Realm.getInstance(config)

        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val map = GameMapRealm(name = name)
            realmTransaction.insert(map)
        }
    }

    suspend fun updateMapBlocks(id: UUID, blocks: MutableList<MutableList<MapBlock>>) {
        val realm = Realm.getInstance(config)

        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val map = realmTransaction
                .where(GameMapRealm::class.java)
                .equalTo("id", id)
                .findFirst()

            map?.blocks = blocks
        }
    }

    suspend fun retrieveMap(id: UUID): GameMap? {
        val realm = Realm.getInstance(config)
        var gameMap: GameMap? = null

        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val map = realmTransaction
                .where(GameMapRealm::class.java)
                .equalTo("id", id)
                .findFirst()
            if (map != null){
                gameMap = GameMap(
                    id = map.id,
                    name = map.name,
                    size = Size.parseSize(map.size),
                    blocks = map.blocks
                )}
        }
        return gameMap
    }

    suspend fun retrieveMaps(): List<GameMap> {
        val realm = Realm.getInstance(config)
        val gameMaps = mutableListOf<GameMap>()

        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            gameMaps.addAll(realmTransaction
                .where(GameMapRealm::class.java)
                .findAll()
                .sort("name", Sort.ASCENDING)
                .map { map ->
                    GameMap(
                        id = map.id,
                        name = map.name,
                        size = Size.parseSize(map.size),
                        blocks = map.blocks,
                    )
                }
            )
        }
        return gameMaps
    }

    suspend fun removeMap(id: UUID) {
        val realm = Realm.getInstance(config)

        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val mapToRemove = realmTransaction
                .where(GameMapRealm::class.java)
                .equalTo("id", id)
                .findFirst()
            mapToRemove?.deleteFromRealm()
        }
    }

    private fun mapGameMapBlocksToRealm(gameBlocks: MutableList<MutableList<MapBlock>>){
        !
    }

    private fun mapRealmMapBlocksToGame(realmBlocks: RealmList<MapBlockRealm>){
        !
    }

}

