package com.scand.internship.mars_scout.mapeditor

import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scand.internship.mars_scout.mapeditor.model.GameMap

class MapEditorViewModel : ViewModel() {

    private val _mapGenerating = MutableLiveData(false)
    val mapGenerating: LiveData<Boolean> = _mapGenerating

    fun generateMap(): GameMap {

        return GameMap("1","1", Size(0,0), null)

    }

}