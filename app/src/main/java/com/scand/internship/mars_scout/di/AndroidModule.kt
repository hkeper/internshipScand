package com.scand.internship.mars_scout.di

import com.scand.internship.mars_scout.MainActivity
import com.scand.internship.mars_scout.launch.LaunchFragment
import com.scand.internship.mars_scout.mapeditor.MapEditorFragment
import com.scand.internship.mars_scout.mapeditor.SaveMapDialog
import com.scand.internship.mars_scout.maplist.MapListFragment
import com.scand.internship.mars_scout.maplist.MapListViewModel
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun launchFragment(): LaunchFragment

    @ContributesAndroidInjector
    abstract fun mapEditorFragment(): MapEditorFragment

    @ContributesAndroidInjector
    abstract fun mapListFragment(): MapListFragment

    @ContributesAndroidInjector
    abstract fun saveMapFragment(): SaveMapDialog

}