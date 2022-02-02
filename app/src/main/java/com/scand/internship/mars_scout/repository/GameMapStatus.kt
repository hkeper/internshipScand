package com.scand.internship.mars_scout.repository

sealed class GameMapStatus{
    object Loading : GameMapStatus()
    object Added : GameMapStatus()
    object Deleted : GameMapStatus()
}
