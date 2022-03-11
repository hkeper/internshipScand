package com.scand.internship.mars_scout.firebase

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.scand.internship.mars_scout.models.*
import javax.inject.Inject
import kotlin.random.Random


class FirebaseDatabaseManager @Inject constructor(private val database: FirebaseDatabase) : FirebaseDatabaseInterface {

    private val KEY_MAP = "map"
    private val KEY_BLOCK = "block"
    private val KEY_BLOCK_TYPE = "type"

    override fun addMap(map: GameMap) {

        val putMap = map.mapToResponseMap()

        database.reference
            .child(KEY_MAP)
            .child(map.id.toString())
            .setValue(putMap)

    }


}