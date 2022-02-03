//package com.scand.internship.mars_scout.di
//
//import com.scand.internship.mars_scout.realm.GameMapDatabaseOperations
//import com.scand.internship.mars_scout.repository.GameMapRepository
//import com.scand.internship.mars_scout.repository.GameMapRepositoryImpl
//import dagger.Module
//import dagger.Provides
//import io.realm.RealmConfiguration
//import javax.inject.Singleton
//
//@Module
//class MapsModule {
//
//    private val realmVersion = 1L
//
//    @Singleton
//    @Provides
//    fun providesRealmConfig(): RealmConfiguration =
//        RealmConfiguration.Builder()
//            .schemaVersion(realmVersion)
////            .migration(migration)
//            .build()
//
//    @Singleton
//    @Provides
//    fun providesMapsRepository(databaseOperations: GameMapDatabaseOperations): GameMapRepository =
//        GameMapRepositoryImpl(databaseOperations)
//
//}