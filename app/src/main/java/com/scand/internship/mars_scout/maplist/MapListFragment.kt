package com.scand.internship.mars_scout.maplist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.scand.internship.mars_scout.R
import com.scand.internship.mars_scout.databinding.LaunchFragmentBinding
import com.scand.internship.mars_scout.databinding.MapEditorFragmentBinding
import com.scand.internship.mars_scout.databinding.MapListFragmentBinding
import com.scand.internship.mars_scout.launch.LaunchFragmentDirections
import com.scand.internship.mars_scout.mapeditor.MapEditorViewModel
import com.scand.internship.mars_scout.models.GameMap


class MapListFragment : Fragment() {

    private val viewModel: MapListViewModel by lazy {
        ViewModelProvider(this)[MapListViewModel::class.java]
    }

    private lateinit var binding: MapListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MapListFragmentBinding.inflate(inflater)

        binding.createMap.setOnClickListener {
            this.findNavController().navigate(
                MapListFragmentDirections.actionMapListFragmentToMapEditorFragment(
                    GameMap("", null)
                ))
        }

        val adapter =
            MapListAdapter(MapListAdapter.MapListListener {
                MapListFragmentDirections.actionMapListFragmentToMapEditorFragment(it)
            })

        binding.mapsRecycler.adapter = adapter

        viewModel.maps.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.mapListRefresh.setOnRefreshListener { viewModel.setLoadingToFalse() }

        return binding.root
    }

}