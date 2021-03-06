package com.scand.internship.mars_scout.mapeditor

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Size
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.scand.internship.mars_scout.R
import com.scand.internship.mars_scout.databinding.MapEditorFragmentBinding
import com.scand.internship.mars_scout.models.BlockType
import com.scand.internship.mars_scout.models.MapBlock
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.scand.internship.mars_scout.models.GameMap
import com.scand.internship.mars_scout.repository.GameMapStatus
import com.scand.internship.mars_scout.utils.find_path.findPath
import com.scand.internship.mars_scout.utils.find_path.setStartPointUtil
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class MapEditorFragment : Fragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MapEditorViewModel by viewModels { viewModelFactory }
    private var listUIMapBlocks: MutableList<MutableList<ImageView>> = mutableListOf()
    private var listChooseMapBlockTypes: MutableList<ImageView> = mutableListOf()
    private lateinit var gameUIMap: GameMap
    private val dialog = SaveMapDialog()
    private lateinit var binding: MapEditorFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapEditorFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        viewModel.gameMap.value?.let { gameUIMap = it }

        generateUIGameMap(GameMap.DEFAULT_SIZE)
        getImageViewMapBlocks()
        getChooseMapBlockTypes()

        viewModel.gameMap.observe(viewLifecycleOwner){
            it?.let {
                gameUIMap = it
                setIDAndImagesForMapBlocks(gameUIMap)
                setStartPoint()
            }
        }

        setTouchOnMapView()
        viewModel.getEditedMapByID()
        viewModel.gameMapStatus.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = true
            binding.map.isVisible = false
            when (status) {
                GameMapStatus.Loading -> binding.progress.isVisible = true
                is GameMapStatus.MapRetrieved -> status.map?.let {
                    viewModel.putUIMapToModelMap(it)
                    binding.progress.isVisible = false
                    binding.map.isVisible = true
                }
                GameMapStatus.Added -> { Toast.makeText(context, "Map ${gameUIMap.name} saved",
                    Toast.LENGTH_SHORT).show()
                    binding.progress.isVisible = false
                    binding.map.isVisible = true
                }
                else -> {
                    binding.progress.isVisible = false
                    binding.map.isVisible = true
                }
            }
        }

        binding.generateMap.setOnClickListener {
            // Doesn't work resources.getDimension(R.dimen.default_map_size_x).toInt()
            viewModel.generateMap(GameMap.DEFAULT_SIZE)
        }

        binding.clearMap.setOnClickListener {
            // Doesn't work resources.getDimension(R.dimen.default_map_size_x).toInt()
            viewModel.clearMap()
            clearMapBlocks()
        }

        binding.saveMap.setOnClickListener {
            putUIMapToViewModelMap()
            if (viewModel.isEditMode.value == true){
                viewModel.saveUIMapToModelMap()
            }else{
                dialog.show(childFragmentManager, "Save Map dialog")
            }
        }

        binding.newMap.setOnClickListener {
            // Doesn't work resources.getDimension(R.dimen.default_map_size_x).toInt()
            viewModel.clearMap()
            clearMapBlocks()
            viewModel.setEditMode(false)
        }

        binding.findWay.setOnClickListener {
            if(!isMapCompletelyFilled(gameUIMap)){
                Toast.makeText(
                    context, "Please fill all map blocks and try again!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val path = findPath(gameUIMap)
                if (path.isNullOrEmpty()) {
                    Toast.makeText(
                        context, "Please fill all map blocks correctly(path without Pit must exists) and try again!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        disableViews()
                        animateMarsScoutPath(path)
                        enableViews()
                    }
                }
            }
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

    }

    private suspend fun animateMarsScoutPath(path: MutableList<Int>) {

        if(!listUIMapBlocks.isNullOrEmpty()) {
            for(p in 0 until path.size) {

                for (y in 0 until listUIMapBlocks.size) {

                    for (x in 0 until listUIMapBlocks[y].size) {
                        val b = listUIMapBlocks[y][x]
                        if(b.id == path[p]) {
                            val temp = b.tag
                            b.setImageResource(R.drawable.rover_logo)
                            delay(500)
                            if (temp != null) {
                                b.setImageResource(temp as Int)
                            } else b.setImageResource(0)
                            b.alpha = 0.5F
                        }
                    }
                }
            }
        }
    }

    private fun disableViews() {
        val mainView = binding.container
        val chooseBlocks = binding.chooseBlockSection

        for(v in 0 until mainView.childCount){
            val subView: View = mainView.getChildAt(v)
            subView.isEnabled = false
        }
        for(v in 0 until chooseBlocks.childCount){
            val subView: View = chooseBlocks.getChildAt(v)
            subView.isEnabled = false
        }

    }

    private fun enableViews() {
        val mainView = binding.container
        val chooseBlocks = binding.chooseBlockSection

        for(v in 0 until mainView.childCount){
            val subView: View = mainView.getChildAt(v)
            subView.isEnabled = true
        }
        for(v in 0 until chooseBlocks.childCount){
            val subView: View = chooseBlocks.getChildAt(v)
            subView.isEnabled = true
        }
    }

    private fun isMapCompletelyFilled(map: GameMap): Boolean {
        var mapFilled = true

        val mapBlocks = map.blocks

        if(!mapBlocks.isNullOrEmpty() && map.size != null) {

            if (mapBlocks.size != map.size.height){
                return false
            }

            for (y in 0 until mapBlocks.size) {

                if (mapBlocks[y].size != map.size.width){
                    return false
                }

                for (x in 0 until mapBlocks[y].size) {
                    val b = mapBlocks[y][x]
                    if(b.type == null){
                        return false
                    }
                }
            }
        }else{
            mapFilled = false
        }

        return mapFilled
    }

    private fun generateUIGameMap(size: Size) {
        val mapView = binding.map
        val lineNum = size.height
        val blocksNum = size.width

        for (y in 0 until lineNum) {
            val linearLayout = LinearLayout(requireContext())
            linearLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt())
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.gravity = Gravity.CENTER_HORIZONTAL

            for (x in 0 until blocksNum) {
                val imageView = ImageView(requireContext())
                imageView.layoutParams = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt()
                )
                imageView.setBackgroundResource(R.drawable.border)
                linearLayout.addView(imageView)
            }
            mapView.addView(linearLayout)
        }
    }

    private fun setIDAndImagesForMapBlocks(map: GameMap) {

        val mapBlocks = map.blocks

        if(!mapBlocks.isNullOrEmpty()) {

            for (y in 0 until mapBlocks.size) {

                for (x in 0 until mapBlocks[y].size) {
                    val b = mapBlocks[y][x]
                    if(b.coordinates != null) {
                        val bX = b.coordinates[0]
                        val bY = b.coordinates[1]
                        listUIMapBlocks[bY][bX].alpha = 1F
                        listUIMapBlocks[bY][bX].id = b.id
                        listUIMapBlocks[bY][bX].contentDescription = b.type.toString()
                        val img: Drawable? = setImageAccordingToType(b.type)
                        listUIMapBlocks[bY][bX].background = img
                    }
                }
            }
        }else{
            clearMapBlocks()
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
            BlockType.GROUND -> ResourcesCompat.getDrawable(
                resources,
                R.drawable.ground,
                null
            )
            else -> ResourcesCompat.getDrawable(
                resources,
                R.drawable.border,
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
                        background = setImageAccordingToType(viewModel.typeChosenMapBlock.value)
                        putUIMapToViewModelMap()
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        getImageViewMapBlockThatInsideMap(event, viewModel.typeChosenMapBlock.value).
                        background = setImageAccordingToType(viewModel.typeChosenMapBlock.value)
                        putUIMapToViewModelMap()
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        getImageViewMapBlockThatInsideMap(event, viewModel.typeChosenMapBlock.value).
                        background = setImageAccordingToType(viewModel.typeChosenMapBlock.value)
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        getImageViewMapBlockThatInsideMap(event, viewModel.typeChosenMapBlock.value).
                        background = setImageAccordingToType(viewModel.typeChosenMapBlock.value)
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
                    listUIMapBlocks[y][x].id = Random.nextInt()
                    listUIMapBlocks[y][x].contentDescription = type.toString()

                    listUIMapBlocks[y][x].background = setImageAccordingToType(type)

                    return listUIMapBlocks[y][x]
                }
            }
        }
        return ImageView(context)
    }

    private fun clearMapBlocks(){

        for (y in 0 until listUIMapBlocks.size) {

            for (x in 0 until listUIMapBlocks[y].size) {
                listUIMapBlocks[y][x].setBackgroundResource(R.drawable.border)
                listUIMapBlocks[y][x].contentDescription = ""
                listUIMapBlocks[y][x].alpha = 1F
            }
        }
    }

    private fun putUIMapToViewModelMap(){
        val blocks : MutableList<MutableList<MapBlock>> = mutableListOf()

        for (y in 0 until listUIMapBlocks.size) {
            val blocksLine = mutableListOf<MapBlock>()

            for (x in 0 until listUIMapBlocks[y].size) {

                if(!listUIMapBlocks[y][x].contentDescription.isNullOrEmpty()) {
                    blocksLine.add(
                        MapBlock(
                            listUIMapBlocks[y][x].id,
                            BlockType.setMapBlockTypeAccordingToUIMapBlockDesc(
                                listUIMapBlocks[y][x].contentDescription as String),
                            mutableListOf(x,y)
                        )
                    )
                } else {
                    listUIMapBlocks[y][x].setBackgroundResource(R.drawable.border)
                    blocksLine.add(
                        MapBlock(Random.nextInt(), null, mutableListOf(x,y))
                    )
                }
            }
            blocks.add(blocksLine)
        }
        viewModel.putUIMapToModelMap(GameMap(gameUIMap.id, gameUIMap.name, gameUIMap.size, blocks))
    }

    private fun setStartPoint(){
        val start = setStartPointUtil(gameUIMap)
        val img: Drawable? = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ground,
            null
        )
        val lastLine = gameUIMap.size?.height?.minus(1) ?: 0

        if(!gameUIMap.blocks.isNullOrEmpty()) {
            gameUIMap.blocks?.get(0)?.get(start)?.type = BlockType.GROUND
            gameUIMap.blocks?.get(lastLine)?.get(start)?.type = BlockType.GROUND
        }
        listUIMapBlocks[0][start].background = img
        listUIMapBlocks[0][start].setImageResource(R.drawable.start)
        listUIMapBlocks[0][start].tag = R.drawable.start
        listUIMapBlocks[lastLine][start].background = img
        listUIMapBlocks[lastLine][start].setImageResource(R.drawable.finish)
        listUIMapBlocks[lastLine][start].tag = R.drawable.finish
    }

}