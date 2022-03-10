package com.scand.internship.mars_scout.firebase

import com.scand.internship.mars_scout.models.GameMap

interface FirebaseDatabaseInterface {

    fun addMap(map: GameMap)

//    fun addNewJoke(joke: Joke, onResult: (Boolean) -> Unit)
//
//    fun getFavoriteJokes(userId: String, onResult: (List<Joke>) -> Unit)
//
//    fun changeJokeFavoriteStatus(joke: Joke, userId: String)
//
//    fun createUser(id: String, name: String, email: String)
//
//    fun getProfile(id: String, onResult: (User) -> Unit)
}