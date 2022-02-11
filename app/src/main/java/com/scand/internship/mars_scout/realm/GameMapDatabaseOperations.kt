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
import javax.inject.Inject
import kotlin.random.Random

class GameMapDatabaseOperations @Inject constructor(private val config: RealmConfiguration) {

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

            map?.blocks = mapGameMapBlocksToRealm(blocks)
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
                    blocks = mapRealmMapBlocksToGame(map.blocks, Size.parseSize(map.size))
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
                        blocks = mapRealmMapBlocksToGame(map.blocks, Size.parseSize(map.size)),
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

    private fun mapGameMapBlocksToRealm(gameBlocks: MutableList<MutableList<MapBlock>>
    ): RealmList<MapBlockRealm> {
        val realmList = RealmList<MapBlockRealm>()
        var blockRealm: MapBlockRealm
        val type = BlockTypeRealm()

        if(!gameBlocks.isNullOrEmpty()) {

            for (y in 0 until gameBlocks.size) {

                for (x in 0 until gameBlocks[y].size) {
                    type.enumField = gameBlocks[y][x].type
                    blockRealm = MapBlockRealm(
                        id = gameBlocks[y][x].id,
                        type = type,
                        coordinates = RealmList(gameBlocks[y][x].coordinates?.get(0),
                            gameBlocks[y][x].coordinates?.get(1)
                        )
                    )
                    realmList.add(blockRealm)
                }
            }
        }
        return realmList
    }

    private fun mapRealmMapBlocksToGame(realmBlocks: RealmList<MapBlockRealm>?, mapSize: Size):
            MutableList<MutableList<MapBlock>>
    {
        val gameBlocks : MutableList<MutableList<MapBlock>> = mutableListOf()

        if(!realmBlocks.isNullOrEmpty()) {
            for (y in 0 until mapSize.height) {
                val blocksLine = mutableListOf<MapBlock>()
                for (x in 0 until mapSize.width) {
                    val i = 0
                    if (i < realmBlocks.size && realmBlocks[i] != null) {
                        gameBlocks[y][x] = MapBlock(
                            id = realmBlocks[i]?.id ?: Random.nextInt(),
                            type = realmBlocks[i]?.type?.enumField,
                            coordinates = realmBlocks[i]?.coordinates
                        )
                        i.inc()
                    } else gameBlocks[y][x] = MapBlock()
                    blocksLine.add(gameBlocks[y][x])
                }
                gameBlocks.add(blocksLine)
            }
        }

        return gameBlocks
    }

}

