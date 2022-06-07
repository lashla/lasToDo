package com.lasha.lastodo.ui.bottom_sheet

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lasha.lastodo.R
import com.lasha.lastodo.data.model.Todos
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_edit_dialog.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@AndroidEntryPoint
class AddEditDialogFragment: BottomSheetDialogFragment() {

    private val navArgs by navArgs<AddEditDialogFragmentArgs>()

    private lateinit var viewModel: AddEditViewModel
    private var filePathUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_edit_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[AddEditViewModel::class.java]
        setupBottomSheetButtons()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            filePathUri = data!!.data
        }
    }

    private fun setupBottomSheetButtons(){
        if (navArgs.currentTodo != null){
            val editTodoText = "Edit todo"
            addEditBtn.text = editTodoText
        }
        deadlineBtn.setOnClickListener{
            selectDatePicker()
        }
        addImageBtn.setOnClickListener {
            openGalleryForImage()
        }
        addEditBtn.setOnClickListener {
            if (navArgs.currentTodo != null){
                editTodo()
                dismiss()
            } else {
                populateTodos()
                dismiss()
            }
        }
    }

    private fun populateTodos(){
        val currentDate = LocalDateTime.now()
        if (titleEt.text.isNotEmpty() && descriptionEt.text.isNotEmpty()){
            viewModel.insertHandler(titleEt.text.toString(), descriptionEt.text.toString(), currentDate.toString(), filePathUri.toString(), deadlineBtn.text.toString())
        }
    }

    private fun editTodo(){
        val currentDate = LocalDateTime.now()
        if (titleEt.text.isNotEmpty() && descriptionEt.text.isNotEmpty()){
            viewModel.updateTodo(Todos(navArgs.currentTodo!!.id, titleEt.text.toString(), descriptionEt.text.toString(), currentDate.toString(), filePathUri.toString(), deadlineBtn.text.toString()))
        }
    }
    private fun selectDatePicker(){
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day  = myCalendar.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), R.style.datePicker,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                val date = LocalDate.of(selectedYear,selectedMonth,selectedDayOfMonth)
                deadlineBtn.text = date.toString()
            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = System.currentTimeMillis()
        dpd.show()
    }

    private fun openGalleryForImage(){
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, 1)
    }

}