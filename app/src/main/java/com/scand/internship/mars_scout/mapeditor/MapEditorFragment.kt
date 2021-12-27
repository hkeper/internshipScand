package com.scand.internship.mars_scout.mapeditor

import android.R.attr
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.scand.internship.mars_scout.R
import com.scand.internship.mars_scout.databinding.MapEditorFragmentBinding
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.MapBlock
import android.R.attr.button
import androidx.core.content.ContextCompat


class MapEditorFragment : Fragment() {

//    private lateinit var viewModel: MapEditorViewModel

    private val viewModel: MapEditorViewModel by lazy {
        ViewModelProvider(this)[MapEditorViewModel::class.java]
    }

    private lateinit var binding: MapEditorFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MapEditorFragmentBinding.inflate(inflater)

        binding.viewModel = viewModel

        binding.generateMap.setOnClickListener {
            setImageBlocks(Size(16,16))
        }

        binding.groundBlockImg.setOnClickListener {
            val colorBackground = (it.background as ColorDrawable).color

            if(colorBackground == ContextCompat.getColor(requireActivity(), R.color.green_chosen)){
                it.setBackgroundResource(R.color.white)
                viewModel.onBlockNotChosen()
            }else {
                it.setBackgroundResource(R.color.green_chosen)
                viewModel.onBlockChosen(BlockType.GROUND)
            }
        }
        binding.pitBlockImg.setOnClickListener {
            val colorBackground = (it.background as ColorDrawable).color

            if(colorBackground == ContextCompat.getColor(requireActivity(), R.color.green_chosen)){
                it.setBackgroundResource(R.color.white)
                viewModel.onBlockNotChosen()
            }else {
                it.setBackgroundResource(R.color.green_chosen)
                viewModel.onBlockChosen(BlockType.PIT)
            }
        }
        binding.sandBlockImg.setOnClickListener {
            val colorBackground = (it.background as ColorDrawable).color

            if(colorBackground == ContextCompat.getColor(requireActivity(), R.color.green_chosen)){
                it.setBackgroundResource(R.color.white)
                viewModel.onBlockNotChosen()
            }else {
                it.setBackgroundResource(R.color.green_chosen)
                viewModel.onBlockChosen(BlockType.SAND)
            }
        }
        binding.hillBlockImg.setOnClickListener {
            val colorBackground = (it.background as ColorDrawable).color

            if(colorBackground == ContextCompat.getColor(requireActivity(), R.color.green_chosen)){
                it.setBackgroundResource(R.color.white)
                viewModel.onBlockNotChosen()
            }else {
                it.setBackgroundResource(R.color.green_chosen)
                viewModel.onBlockChosen(BlockType.HILL)
            }
        }

        return binding.root
    }

    private fun setImageBlocks(mapSize: Size) {

        val mapLayout = binding.map
        val mapBlocks = viewModel.generateMap(mapSize)?.blocks

        //Timber.d("!!! ${yourLayout.childCount}")
        if (mapBlocks != null) {

            val mapBlocksSplit: List<List<MapBlock>> = mapBlocks.chunked(mapSize.width)

            for (y in 0 until mapSize.height) {

                val subView: View = mapLayout.getChildAt(y)

                if (subView is LinearLayout) {

                    for (x in 0 until mapSize.width) {
                        val subSubView: View = subView.getChildAt(x)

                        if (subSubView is ImageView) {
                            val imageView: ImageView = subSubView

                            imageView.id = mapBlocksSplit[y][x].id

                            val img: Drawable? = when (mapBlocksSplit[y][x].type) {
                                BlockType.GROUND -> ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.ground,
                                    null
                                )
                                BlockType.HILL -> ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.hill,
                                    null
                                )
                                BlockType.PIT -> ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.pit,
                                    null
                                )
                                else -> ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.sand,
                                    null
                                )
                            }

                            imageView.setImageDrawable(img)
                        }
                    }
                }
            }
        }
    }

    private fun setWhiteBackground(){
        val layout = binding.chooseBockSection

        for (i in 0 until layout.childCount) {
            val subView: View = layout.getChildAt(i)

            if (subView is ImageView) {
                subView.setBackgroundResource(R.color.white)
            }
        }

    }


}