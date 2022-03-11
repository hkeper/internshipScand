package com.scand.internship.mars_scout.di

import com.scand.internship.mars_scout.firebase.FirebaseDatabaseInterface
import com.scand.internship.mars_scout.firebase.FirebaseDatabaseManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [FirebaseModule::class])
abstract class InteractionModule {

    @Binds
    @Singleton
    abstract fun database(database: FirebaseDatabaseManager): FirebaseDatabaseInterface
}