package com.scand.internship.mars_scout.maplist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.scand.internship.mars_scout.R
import com.scand.internship.mars_scout.databinding.MapListFragmentBinding
import com.scand.internship.mars_scout.repository.GameMapStatus
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class MapListFragment : Fragment() {

    @Inject
    lateinit var adapter: MapListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MapListViewModel by viewModels { viewModelFactory }
    private lateinit var binding: MapListFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MapListFragmentBinding.inflate(inflater)
        val recyclerView = binding.mapsRecycler
        adapter = MapListAdapter()
        recyclerView.adapter = adapter
        val swipeLayout = binding.mapListRefresh

        viewModel.getMapsListFromDB()

        viewModel.gameMapStatus.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = true
            binding.mapListRefresh.isVisible = false
            when (status) {
                GameMapStatus.Loading -> binding.progress.isVisible = true
                is GameMapStatus.MapsRetrieved -> {
                    viewModel.putMapsToViewModelList(status.maps)
                    binding.progress.isVisible = false
                    binding.mapListRefresh.isVisible = true
                }
                else -> {
                    binding.progress.isVisible = false
                    binding.mapListRefresh.isVisible = true
                }
            }
        }

        binding.createMap.setOnClickListener {
            this.findNavController().navigate(
                MapListFragmentDirections.actionMapListFragmentToMapEditorFragment())
        }

        binding.synchronizeMaps.setOnClickListener {
            viewModel.getMapsListFromDB()
        }

        viewModel.maps.observe(viewLifecycleOwner, {
            if(it.isNullOrEmpty()){
                binding.mapListRefresh.isVisible = false
                binding.messageEmptyMapList.isVisible = true
            }else {
                binding.mapListRefresh.isVisible = true
                binding.messageEmptyMapList.isVisible = false
                adapter.submitList(it)
            }
        })

        swipeLayout.setOnRefreshListener {
            viewModel.setLoadingToFalse()
            swipeLayout.isRefreshing = false
        }

        setUpRecyclerView(recyclerView)

        return binding.root
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {

        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(recyclerView) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val deleteButton = deleteButton(position)
                val markAsUnreadButton = editButton(position)
                return listOf(deleteButton, markAsUnreadButton)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    viewModel.maps.value?.get(position)
                        ?.let { viewModel.removeMapAtPosition(position, it.id) }
                    adapter.notifyItemRemoved(position)
                }
            })
    }

    private fun editButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            "Edit",
            14.0f,
            R.color.purple_500,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    if(!viewModel.maps.value.isNullOrEmpty()){
                        viewModel.maps.value?.get(position)
                            ?.let { viewModel.setEditedMapID(it.id)}
                        findNavController().navigate(MapListFragmentDirections.actionMapListFragmentToMapEditorFragment())
                    } else {
                        findNavController().navigate(MapListFragmentDirections.actionMapListFragmentToMapEditorFragment())
                    }

                }
            })
    }

}