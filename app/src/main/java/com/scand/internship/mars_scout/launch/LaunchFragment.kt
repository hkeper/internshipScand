package com.scand.internship.mars_scout.launch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.scand.internship.mars_scout.databinding.LaunchFragmentBinding
import com.scand.internship.mars_scout.models.GameMap

class LaunchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = LaunchFragmentBinding.inflate(inflater)

        binding.editorButton.setOnClickListener {
            this.findNavController().navigate(
                LaunchFragmentDirections.actionLaunchFragmentToMapEditorFragment(
                    GameMap("test")
                ))
        }

        binding.listButton.setOnClickListener {
            this.findNavController().navigate(
                LaunchFragmentDirections.actionLaunchFragmentToMapListFragment())
        }

        return binding.root
    }


}