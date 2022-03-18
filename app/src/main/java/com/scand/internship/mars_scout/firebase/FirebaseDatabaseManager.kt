package com.scand.internship.mars_scout.firebase

import com.google.firebase.database.*
import com.scand.internship.mars_scout.models.*
import java.util.*
import javax.inject.Inject


class FirebaseDatabaseManager @Inject constructor(private val database: FirebaseDatabase) : FirebaseDatabaseInterface {

    private val KEY_MAP = "map"
    private val KEY_BLOCKS = "blocks"

    override fun addMap(map: GameMap) {
        val putMap = map.mapToMapDB()

        database.reference
            .child(KEY_MAP)
            .child(map.id.toString())
            .setValue(putMap)
    }

    override fun getMap(id: UUID, onResult: (GameMap) -> Unit) {
        database.reference
            .child(KEY_MAP)
            .child(id.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) = Unit

                override fun onDataChange(snapshot: DataSnapshot) {
                    val map = snapshot.getValue(MapResponse::class.java)
                    val blocksUI: MutableList<MutableList<MapBlock>> = mutableListOf()

                        for (blocksLine in snapshot.child(KEY_BLOCKS).children){
                            val blockLineUI = mutableListOf<MapBlock>()
                            for(block in blocksLine.children){
                                val blockResponse = block.getValue(MapBlockResponse::class.java)
                                    ?.mapToUIBlock()
                                if (blockResponse != null) {
                                    blockLineUI.add(blockResponse)
                                }
                            }
                            blocksUI.add(blockLineUI)
                        }

                    if (map != null) {
                        if (map.isValid()) {
                            val mapUI = map.mapResponseToMapUI()

                            map.run {
                                onResult(GameMap(mapUI.id, mapUI.name, mapUI.size, blocksUI))
                            }
                        }
                    }
                }

            })
    }

    override fun deleteMap(id: UUID) {
        database.reference
            .child(KEY_MAP)
            .child(id.toString())
            .removeValue()
    }

    override suspend fun getMapsList(onResult: (List<GameMap>) -> Unit) {
        val list = mutableListOf<GameMap>()

         database.reference
            .child(KEY_MAP)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) = onResult(list.toList())

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (mapItem in snapshot.children){
                        val map = mapItem.getValue(MapResponse::class.java)

                        val blocksUI: MutableList<MutableList<MapBlock>> = mutableListOf()

                        for (blocksLine in mapItem.child(KEY_BLOCKS).children){
                            val blockLineUI = mutableListOf<MapBlock>()
                            for(block in blocksLine.children){
                                val blockResponse = block.getValue(MapBlockResponse::class.java)
                                    ?.mapToUIBlock()
                                if (blockResponse != null) {
                                    blockLineUI.add(blockResponse)
                                }
                            }
                            blocksUI.add(blockLineUI)
                        }

                        val mapUI = map?.mapResponseToMapUI()

                        if (mapUI != null) {
                            list.add(GameMap(mapUI.id, mapUI.name, mapUI.size, blocksUI))
                        }
                    }
                    snapshot.run{
                        onResult(list.toList())
                    }
                }
            })
    }

    override fun clearDB() {
        database.reference
            .setValue(null)
    }
}