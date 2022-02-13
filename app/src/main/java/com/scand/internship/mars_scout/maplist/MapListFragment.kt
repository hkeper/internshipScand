package com.scand.internship.mars_scout.maplist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.scand.internship.mars_scout.R
import com.scand.internship.mars_scout.databinding.MapListFragmentBinding
import com.scand.internship.mars_scout.models.GameMap
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class MapListFragment : Fragment() {

    @Inject
    lateinit var adapter: MapListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

//    private val viewModel: MapListViewModel by lazy {
//        ViewModelProvider(this)[MapListViewModel::class.java]
//    }

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

        binding.createMap.setOnClickListener {
            this.findNavController().navigate(
                MapListFragmentDirections.actionMapListFragmentToMapEditorFragment(
                    GameMap("")
                ))
        }

        viewModel.maps.observe(viewLifecycleOwner, {
            it?.let {
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
                    viewModel.removeMapAtPosition(position)
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
                        findNavController().navigate(MapListFragmentDirections.actionMapListFragmentToMapEditorFragment(
                            viewModel.maps.value!![position]
                        ))
                        val i = viewModel.maps.value!![position].id
                        viewModel.setEditedMapID(viewModel.maps.value!![position].id)
                    } else {
                        findNavController().navigate(MapListFragmentDirections.actionMapListFragmentToMapEditorFragment(
                            GameMap("")
                        ))
                    }

                }
            })
    }

}