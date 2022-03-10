package com.scand.internship.mars_scout

import com.scand.internship.mars_scout.di.FirebaseModule
import com.scand.internship.mars_scout.firebase.FirebaseDatabaseInterface
import com.scand.internship.mars_scout.firebase.FirebaseDatabaseManager
import dagger.Binds
import dagger.Module

@Module(includes = [FirebaseModule::class])
abstract class InteractionModule {

    @Binds
    abstract fun database(database: FirebaseDatabaseManager): FirebaseDatabaseInterface
}