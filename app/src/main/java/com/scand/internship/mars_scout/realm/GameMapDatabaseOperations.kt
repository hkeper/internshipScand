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

    suspend fun insertMap(gameMap: GameMap) {
        val realm = Realm.getInstance(config)
        val blocksRealm = gameMap.blocks?.let { mapGameMapBlocksToRealm(it) }

        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val map = GameMapRealm(
                id = gameMap.id,
                name = gameMap.name,
                size = gameMap.size.toString(),
                blocks = blocksRealm
            )
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

        if(!gameBlocks.isNullOrEmpty()) {

            for (y in 0 until gameBlocks.size) {

                for (x in 0 until gameBlocks[y].size) {
                    val type = BlockTypeRealm()
                    type.enumField = gameBlocks[y][x].type!!
                    val blockRealm = MapBlockRealm(
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
            var i = 0
            val div = realmBlocks.size.toFloat()/mapSize.width.toFloat()

            val gameMapY = if(div> 0 && div <= 1) 1 else div.toInt()

            for (y in 0 until gameMapY) {
                val blocksLine: MutableList<MapBlock> = mutableListOf()
                //gameBlocks.add(blocksLine)
                for (x in 0 until mapSize.width) {
                    if (i < realmBlocks.size && realmBlocks[i] != null) {
                        blocksLine.add(MapBlock(
                            id = realmBlocks[i]?.id ?: Random.nextInt(),
                            type = realmBlocks[i]?.type?.enumField,
                            coordinates = realmBlocks[i]?.coordinates?.toMutableList()
                        ))
                        i += 1
                    } else break
                }
                gameBlocks.add(blocksLine)
            }
        }
        return gameBlocks
    }

    suspend fun clearDB() {
        val realm = Realm.getInstance(config)
        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            realmTransaction
                .deleteAll()
        }
    }

}

