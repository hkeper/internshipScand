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

    private var listMapBlocks: MutableList<MutableList<ImageView>> = mutableListOf()

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

        getImageViewMapBlocks()

        binding.groundBlockImg.setOnTouchListener { v, event ->
            val action = event.action
            when(action){

                MotionEvent.ACTION_DOWN -> {
                    pDownX= event.x.toInt()
                    pDownY= event.y.toInt()
                }


                MotionEvent.ACTION_MOVE -> { }

                MotionEvent.ACTION_UP -> {
                    pUpX= event.x.toInt()
                    pUpY= event.y.toInt()
                }

                MotionEvent.ACTION_CANCEL -> {

                }

                else ->{

                }
            }
            return@OnTouchListener true
        }


        binding.generateMap.setOnClickListener {
            setImageBlocks(Size(R.dimen.default_map_size_x,R.dimen.default_map_size_y))
        }

        //TODO Make with one function using child
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

        for (y in 0 until listMapBlocks.size) {

            for (x in 0 until listMapBlocks[y].size) {

                listMapBlocks[y][x].setOnClickListener {

                    if(viewModel.isBlockChosen.value == true){
                        (it as ImageView).setImageDrawable(setImageAccordingToType(
                            viewModel.typeChosenMapBlock.value))
                    }

                }

            }
        }

        return binding.root
    }

    private fun setImageBlocks(mapSize: Size) {

        val mapBlocks = viewModel.generateMap(mapSize)?.blocks

        if (mapBlocks != null) {

            val mapBlocksSplit: List<List<MapBlock>> = mapBlocks.chunked(mapSize.width)

            for (y in 0 until mapSize.height) {

                for (x in 0 until mapSize.width) {
                    listMapBlocks[y][x].id = mapBlocksSplit[y][x].id
                    val img: Drawable? = setImageAccordingToType(mapBlocksSplit[y][x].type)
                    listMapBlocks[y][x].setImageDrawable(img)
                }
            }
        }
    }

    private fun getImageViewMapBlocks() {

        val mapLayout = binding.map

        for (y in 0 until mapLayout.childCount) {

            val subView: View = mapLayout.getChildAt(y)
            val lineBlocksList: MutableList<ImageView> = mutableListOf()

            if (subView is LinearLayout) {

                for (x in 0 until subView.childCount) {

                    val subSubView: View = subView.getChildAt(x)

                    if (subSubView is ImageView) {
                        val imageView: ImageView = subSubView
                        lineBlocksList.add(imageView)
                    }
                }
                listMapBlocks.add(lineBlocksList)
            }
        }

    }

    private fun setImageAccordingToType(type: BlockType?): Drawable?{
        return when (type) {
            BlockType.SAND -> ResourcesCompat.getDrawable(
                resources,
                R.drawable.sand,
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
                R.drawable.ground,
                null
            )
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