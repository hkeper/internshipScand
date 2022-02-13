package com.scand.internship.mars_scout.di

import androidx.fragment.app.Fragment
import com.scand.internship.mars_scout.MarsScoutApp
import com.scand.internship.mars_scout.mapeditor.MapEditorViewModel
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AndroidModule::class,
        MapsModule::class,
        ViewModelsModule::class
    ]
)

interface AppComponent {
    fun inject(app: MarsScoutApp)
}
