package com.scand.internship.mars_scout.mapeditor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scand.internship.mars_scout.R

class MapEditorFragment : Fragment() {

    private lateinit var viewModel: MapEditorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_editor_fragment, container, false)
    }



}