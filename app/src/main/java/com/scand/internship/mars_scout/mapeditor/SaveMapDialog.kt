package com.scand.internship.mars_scout.mapeditor

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.scand.internship.mars_scout.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SaveMapDialog: DialogFragment() {

//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//    private val viewModel: MapEditorViewModel by viewModels { viewModelFactory }

    private val viewModel: MapEditorViewModel by viewModels({requireParentFragment()})

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.setMessage(R.string.save_dialog_message)
                .setView(inflater.inflate(R.layout.dialog_save_map, null))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, null)

            builder.create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        //Validate and dismiss

                        val mapName = (dialog as Dialog).findViewById<EditText>(R.id.mapname)

                        if(!mapName.text.isNullOrEmpty()){
                            viewModel.setMapName(mapName.text.toString())
                            viewModel.saveUIMapToModelMap()
                            dismiss()
                        }else{
                            Toast.makeText(context, "Please enter Map name",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
