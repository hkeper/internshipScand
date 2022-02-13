package com.scand.internship.mars_scout.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scand.internship.mars_scout.di.viewmodels.ViewModelFactory
import com.scand.internship.mars_scout.di.viewmodels.ViewModelKey
import com.scand.internship.mars_scout.mapeditor.MapEditorViewModel
import com.scand.internship.mars_scout.maplist.MapListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapEditorViewModel::class)
    abstract fun bindMapEditorViewModel(mapEditorViewModel: MapEditorViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(MapEditorViewModel::class)
//    abstract fun bindVMFactory(f: MapEditorViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(MapListViewModel::class)
    abstract fun bindMapListViewModel(mapListViewModel: MapListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}
