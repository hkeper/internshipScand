package com.scand.internship.mars_scout.di

import com.scand.internship.mars_scout.MarsScoutApp
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AndroidModule::class,
        MapsModule::class,
        ViewModelsModule::class,
        InteractionModule::class
    ]
)

interface AppComponent {
    fun inject(app: MarsScoutApp)
}
