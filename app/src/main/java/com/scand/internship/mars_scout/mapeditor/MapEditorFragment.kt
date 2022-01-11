package com.scand.internship.mars_scout.mapeditor

import android.annotation.SuppressLint
import android.graphics.Rect
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
import android.widget.Toast
import androidx.core.content.ContextCompat


class MapEditorFragment : Fragment() {

//    private lateinit var viewModel: MapEditorViewModel

    private var listMapBlocks: MutableList<MutableList<ImageView>> = mutableListOf()
    private var listChooseMapBlockTypes: MutableList<ImageView> = mutableListOf()

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

        setTouchOnView()

//        listMapBlocks[0][0].setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        Toast.makeText(activity, "Down1!!!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                return v?.onTouchEvent(event) ?: true
//            }
//        })

//        listMapBlocks[0][0].setOnTouchListener { v, event ->
//            when (event?.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    Toast.makeText(activity, "Down1!!!", Toast.LENGTH_SHORT).show()
//                }
//            }
//            v?.onTouchEvent(event) ?: true
//        }

//        listMapBlocks[0][1].setOnTouchListener { v, event ->
//            when (event?.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    Toast.makeText(activity, "Down2!!!", Toast.LENGTH_SHORT).show()
//                }
//                MotionEvent.ACTION_HOVER_ENTER -> {
//                    Toast.makeText(activity, "Hover Enter2!!!", Toast.LENGTH_SHORT).show()
//                }
//            }
//            v?.onTouchEvent(event) ?: true
//        }


//        binding.groundBlockImg.setOnTouchListener { v, event ->
//            when (event?.action) {
//                MotionEvent.ACTION_HOVER_ENTER -> {
//                    Toast.makeText(activity, "Down!!!", Toast.LENGTH_SHORT).show()
//                }MotionEvent.ACTION_HOVER_MOVE -> {
//                Toast.makeText(activity, "Move!!!", Toast.LENGTH_SHORT).show()
//            }MotionEvent.ACTION_HOVER_EXIT -> {
//                Toast.makeText(activity, "Up!!!", Toast.LENGTH_SHORT).show()
//            }
//            }
//
//            v?.onTouchEvent(event) ?: true
//        }

        binding.generateMap.setOnClickListener {
            // Doesn't work resources.getDimension(R.dimen.default_map_size_x).toInt()
            setImageBlocks(Size(16,16))
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

//        for (y in 0 until listMapBlocks.size) {
//
//            for (x in 0 until listMapBlocks[y].size) {
//
//                listMapBlocks[y][x].setOnClickListener {
//
//                    if(viewModel.isBlockChosen.value == true){
//                        (it as ImageView).setImageDrawable(setImageAccordingToType(
//                            viewModel.typeChosenMapBlock.value))
//                    }
//
//                }
//
//            }
//        }

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
    private fun setTouchOnView(){

        val mapView = binding.map
//        val subView = binding.groundBlockDesc

//        for (y in 0 until mapView.childCount) {
            // OnTouchListener on the Screen
//            mapView.getChildAt(y).setOnTouchListener { v, event ->
            mapView.setOnTouchListener { v, event ->
                if (viewModel.isBlockChosen.value == true) {

                    return@setOnTouchListener when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {

                            val bt = viewModel.typeChosenMapBlock.value
                            val b = getViewMapBlockThatInsideMap(event)

                            b.setImageDrawable(
                                setImageAccordingToType(viewModel.typeChosenMapBlock.value)
                            )

                            true
                        }
                        MotionEvent.ACTION_MOVE -> {

                            getViewMapBlockThatInsideMap(event).setImageDrawable(
                                setImageAccordingToType(viewModel.typeChosenMapBlock.value)
                            )

                            true
                        }
                        else -> false
                    }

                }
                v?.onTouchEvent(event) ?: true
            }
//        }

    }

    // V shall be the subclass i.e. the subView declared in onCreate function
    // This functions confirms the dimensions of the view (subView in out program)
    private fun isInside(v: View, e: MotionEvent): Boolean {

//        val viewBoundaries = Rect(v.left, v.top, v.right, v.bottom)

        val viewCoordinates = IntArray(2)
        val vp = v.getLocationOnScreen(viewCoordinates)

        val x = e.rawX
        val y = e.rawY

//        return viewBoundaries.contains(x.toInt(), y.toInt())
        var inside = false

        inside = (x >= viewCoordinates[0]) && (x <= (viewCoordinates[0] + v.width)) &&
                (y >= viewCoordinates[1]) && (y <= (viewCoordinates[1] + v.height))

        return inside
    }

    private fun getViewMapBlockThatInsideMap(e: MotionEvent): ImageView {

        val viewBoundaries1 = Rect(listMapBlocks[0][0].left, listMapBlocks[0][0].top, listMapBlocks[0][0].right, listMapBlocks[0][0].bottom)
        val viewBoundaries2 = Rect(listMapBlocks[1][0].left, listMapBlocks[1][0].top, listMapBlocks[1][0].right, listMapBlocks[1][0].bottom)

        for (y in 0 until listMapBlocks.size) {

            for (x in 0 until listMapBlocks[y].size) {

                if (isInside(listMapBlocks[y][x], e)) {
//                                    Toast.makeText(context, "Inside", Toast.LENGTH_SHORT).show()
                    return listMapBlocks[y][x]

                }
            }
        }
        return ImageView(context)
    }

    private fun setWhiteBackground(){
        val layout = binding.chooseBlockSection

        for (i in 0 until layout.childCount) {
            val subView: View = layout.getChildAt(i)

            if (subView is ImageView) {
                subView.setBackgroundResource(R.color.white)
            }
        }

    }


}