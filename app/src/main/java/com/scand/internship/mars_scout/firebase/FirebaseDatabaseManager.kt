package com.scand.internship.mars_scout.firebase

import com.google.firebase.database.*
import com.scand.internship.mars_scout.models.GameMap
import javax.inject.Inject


class FirebaseDatabaseManager @Inject constructor(private val database: FirebaseDatabase) : FirebaseDatabaseInterface {

    private val KEY_MAP = "map"
    private val KEY_BLOCK = "block"
    private val KEY_BLOCK_TYPE = "type"

    override fun addMap(map: GameMap) {

        database.reference
            .child(KEY_MAP)
            .child(map.id.toString())
            .setValue(map)

        database.reference
            .setValue("Hello World!")


    }

//    override fun createUser(id: String, name: String, email: String) {
//        val user = User(id, name, email)
//
//        database.reference.child(KEY_USER).child(id).setValue(user)
//    }
//
//    override fun listenToJokes(onJokeAdded: (Joke) -> Unit) {
//        database.reference.child(KEY_JOKE)
//            .orderByKey()
//            .addChildEventListener(object : ChildEventListener {
//                override fun onCancelled(p0: DatabaseError?) = Unit
//                override fun onChildMoved(p0: DataSnapshot?, p1: String?) = Unit
//                override fun onChildChanged(p0: DataSnapshot?, p1: String?) = Unit
//                override fun onChildRemoved(p0: DataSnapshot?) = Unit
//
//                override fun onChildAdded(snapshot: DataSnapshot?, p1: String?) {
//                    snapshot?.getValue(JokeResponse::class.java)?.run {
//                        if (isValid()) {
//                            onJokeAdded(mapToJoke())
//                        }
//                    }
//                }
//            })
//    }
//
//    override fun addNewJoke(joke: Joke, onResult: (Boolean) -> Unit) {
//        val newJokeReference = database.reference.child(KEY_JOKE).push()
//        val newJoke = joke.copy(id = newJokeReference.key)
//
//        newJokeReference.setValue(newJoke).addOnCompleteListener { onResult(it.isSuccessful && it.isComplete) }
//    }
//
//    override fun getFavoriteJokes(userId: String, onResult: (List<Joke>) -> Unit) {
//        database.reference
//            .child(KEY_USER)
//            .child(userId)
//            .child(KEY_FAVORITE)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError?) = onResult(listOf())
//
//                override fun onDataChange(snapshot: DataSnapshot?) {
//                    snapshot?.run {
//                        val jokes = children.mapNotNull { it.getValue(JokeResponse::class.java) }
//
//                        onResult(jokes.map(JokeResponse::mapToJoke))
//                    }
//                }
//            })
//    }
//
//    override fun changeJokeFavoriteStatus(joke: Joke, userId: String) {
//        val reference = database.reference
//            .child(KEY_USER)
//            .child(userId)
//            .child(KEY_FAVORITE)
//            .child(joke.id)
//
//        reference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError?) {}
//
//            override fun onDataChange(snapshot: DataSnapshot?) {
//                val oldJoke = snapshot?.getValue(JokeResponse::class.java)
//
//                if (oldJoke!=null) {
//                    reference.setValue(null)
//                } else {
//                    reference.setValue(joke)
//                }
//            }
//        })
//    }
//
//    override fun getProfile(id: String, onResult: (User) -> Unit) {
//        database.reference
//            .child(KEY_USER)
//            .child(id)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError?) = Unit
//
//                override fun onDataChange(snapshot: DataSnapshot?) {
//                    val user = snapshot?.getValue(UserResponse::class.java)
//                    val favoriteJokes = snapshot?.child(KEY_FAVORITE)?.children
//                        ?.map { it?.getValue(JokeResponse::class.java) }
//                        ?.mapNotNull { it?.mapToJoke() }
//                        ?: listOf()
//
//
//                    user?.run { onResult(User(id, username, email, favoriteJokes)) }
//                }
//            })
//    }


}