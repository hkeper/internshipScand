package com.scand.internship.mars_scout.repository

import com.scand.internship.mars_scout.models.GameMap

sealed class GameMapStatus{
    object Loading : GameMapStatus()
    object Added : GameMapStatus()
    object BlocksAdded : GameMapStatus()
    object Deleted : GameMapStatus()
    data class MapRetrieved(val map: GameMap?) : GameMapStatus()
    data class MapsRetrieved(val maps: List<GameMap>) : GameMapStatus()
}
