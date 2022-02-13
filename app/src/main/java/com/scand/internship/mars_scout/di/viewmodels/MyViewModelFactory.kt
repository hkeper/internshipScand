package com.scand.internship.mars_scout.di.viewmodels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.scand.internship.mars_scout.mapeditor.MapEditorViewModel
import com.scand.internship.mars_scout.repository.GameMapRepository
import javax.inject.Inject

//class MyViewModelFactory(
//    owner: SavedStateRegistryOwner,
//    private val repository: GameMapRepository,
//    defaultArgs: Bundle? = null
//) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
//    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle
//    ): T {
//        return MapEditorViewModel(
//            repository,
//            handle
//        ) as T
//    }
//}

