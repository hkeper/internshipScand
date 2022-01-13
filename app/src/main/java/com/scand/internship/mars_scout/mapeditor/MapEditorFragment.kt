package com.scand.internship.mars_scout.mapeditor

import android.annotation.SuppressLint
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
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.scand.internship.mars_scout.models.GameMap
import java.util.*


class MapEditorFragment : Fragment() {

    private var listUIMapBlocks: MutableList<MutableList<ImageView>> = mutableListOf()
    private var listChooseMapBlockTypes: MutableList<ImageView> = mutableListOf()
    private lateinit var gameUIMap: GameMap

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
        getChooseMapBlockTypes()

        val id = UUID.randomUUID()
        val blocks = mutableListOf(mutableListOf<MapBlock>())
        gameUIMap = GameMap(id, id.toString(), Size(listUIMapBlocks[0].size, listUIMapBlocks.size), blocks)

        viewModel.gameMap.observe(viewLifecycleOwner){
            it?.let {
                gameUIMap = it
            }
        }

        setTouchOnMapView()

        binding.generateMap.setOnClickListener {
            // Doesn't work resources.getDimension(R.dimen.default_map_size_x).toInt()
            setIDAndImagesForMapBlocks(Size(16,16))
        }

        binding.clearMap.setOnClickListener {
            // Doesn't work resources.getDimension(R.dimen.default_map_size_x).toInt()
            clearMapBlocks()
        }

        binding.saveMap.setOnClickListener {
            // Doesn't work resources.getDimension(R.dimen.default_map_size_x).toInt()
            saveMap()
        }

        binding.groundBlockImg.setOnClickListener {
            choseBlockType(it, BlockType.GROUND)
        }
        binding.pitBlockImg.setOnClickListener {
            choseBlockType(it, BlockType.PIT)
        }
        binding.sandBlockImg.setOnClickListener {
            choseBlockType(it, BlockType.SAND)
        }
        binding.hillBlockImg.setOnClickListener {
            choseBlockType(it, BlockType.HILL)
        }

        return binding.root
    }

    private fun setIDAndImagesForMapBlocks(mapSize: Size) {

        gameUIMap = viewModel.generateMap(mapSize)

        val mapBlocks = gameUIMap.blocks

        if (mapBlocks != null) {

            for (y in 0 until mapSize.height) {

                for (x in 0 until mapSize.width) {

                    listUIMapBlocks[y][x].id = mapBlocks[y][x].id
                    listUIMapBlocks[y][x].contentDescription = mapBlocks[y][x].type.toString()
                    val img: Drawable? = setImageAccordingToType(mapBlocks[y][x].type)
                    listUIMapBlocks[y][x].setImageDrawable(img)
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
                listUIMapBlocks.add(lineBlocksList)
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

    private fun getChooseMapBlockTypes() {
        val chooseBlock = binding.chooseBlockSection

        for (i in 0 until chooseBlock.childCount) {

            val subView: View = chooseBlock.getChildAt(i)

            if (subView is ImageView) {

                listChooseMapBlockTypes.add(subView)

            }
        }
    }

    private fun choseBlockType(view: View, type: BlockType){
        val colorBackground = (view.background as ColorDrawable).color

        if(colorBackground == ContextCompat.getColor(requireActivity(), R.color.green_chosen)){
            view.setBackgroundResource(R.color.white)
            viewModel.onBlockNotChosen()
        }else {
            for (i in 0 until listChooseMapBlockTypes.size){
                listChooseMapBlockTypes[i].setBackgroundResource(R.color.white)
            }
            view.setBackgroundResource(R.color.green_chosen)
            viewModel.onBlockChosen(type)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchOnMapView(){

        val mapView = binding.map

        mapView.setOnTouchListener { v, event ->
            if (viewModel.isBlockChosen.value == true) {
                return@setOnTouchListener when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        getImageViewMapBlockThatInsideMap(event, viewModel.typeChosenMapBlock.value).
                        setImageDrawable(
                            setImageAccordingToType(viewModel.typeChosenMapBlock.value))
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        getImageViewMapBlockThatInsideMap(event, viewModel.typeChosenMapBlock.value).
                        setImageDrawable(
                            setImageAccordingToType(viewModel.typeChosenMapBlock.value))
                        true
                    }
                    else -> false
                }
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    // V shall be the subclass i.e. the subView declared in onCreate function
    // This functions confirms the dimensions of the view (subView in out program)
    private fun isInside(v: View, e: MotionEvent): Boolean {

        val viewCoordinates = IntArray(2)
        v.getLocationOnScreen(viewCoordinates)
        val x = e.rawX
        val y = e.rawY

        return (x >= viewCoordinates[0]) && (x <= (viewCoordinates[0] + v.width)) &&
                (y >= viewCoordinates[1]) && (y <= (viewCoordinates[1] + v.height))
    }

    private fun getImageViewMapBlockThatInsideMap(e: MotionEvent, type: BlockType?): ImageView {

        for (y in 0 until listUIMapBlocks.size) {

            for (x in 0 until listUIMapBlocks[y].size) {

                if (isInside(listUIMapBlocks[y][x], e)) {
                    listUIMapBlocks[y][x].id = ("" + x + y).toInt()
                    listUIMapBlocks[y][x].contentDescription = type.toString()
                    return listUIMapBlocks[y][x]
                }
            }
        }
        return ImageView(context)
    }

    private fun clearMapBlocks(){

        for (y in 0 until listUIMapBlocks.size) {

            for (x in 0 until listUIMapBlocks[y].size) {

                listUIMapBlocks[y][x].setImageDrawable(null)
                listUIMapBlocks[y][x].contentDescription = ""

            }
        }
        viewModel.clearMap()
    }

    private fun saveMap(){
        // TODO add check for empty blocks
        val blocks : MutableList<MutableList<MapBlock>> = mutableListOf()

        for (y in 0 until listUIMapBlocks.size) {

            val blocksLine = mutableListOf<MapBlock>()

            for (x in 0 until listUIMapBlocks[y].size) {

                if(!listUIMapBlocks[y][x].contentDescription.isNullOrEmpty()) {
                    blocksLine.add(
                        MapBlock(
                            listUIMapBlocks[y][x].id, (listUIMapBlocks[y][x].id.toString()),
                            setMapBlockTypeAccordingToUIMapBlockDesc(listUIMapBlocks[y][x].contentDescription as String),
                            Pair(x, y)
                        )
                    )
                } else {
                    blocksLine.add(
                        MapBlock(("" + x + y).toInt(), ("" + x + y),null, Pair(x, y))
                    )
                }
            }
            blocks.add(blocksLine)
        }

        viewModel.saveMap(GameMap(gameUIMap.id, gameUIMap.name, gameUIMap.size, blocks))

    }

    private fun setMapBlockTypeAccordingToUIMapBlockDesc(type: String) : BlockType{
        return when {
            type.contains("SAND", true) -> BlockType.SAND
            type.contains("HILL", true) -> BlockType.HILL
            type.contains("PIT", true) -> BlockType.PIT
            else -> BlockType.GROUND
        }
    }


}