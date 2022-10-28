package com.lasha.lastodo.ui.bottom_sheet

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lasha.lastodo.R
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.utils.CheckInternetConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_edit_dialog.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class AddEditDialogFragment: BottomSheetDialogFragment() {

    private val navArgs by navArgs<AddEditDialogFragmentArgs>()

    private lateinit var viewModel: AddEditViewModel
    private var filePathUri: Uri? = null
    private var timeInMillis: Long = 0

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(requireContext(), R.layout.add_edit_dialog, null)
        view.setBackgroundResource(R.drawable.ic_rectangle_rounded_dialogbc)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[AddEditViewModel::class.java]
        setupBottomSheetButtons()
        super.onViewCreated(view, savedInstanceState)
    }

    private var resultGetFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        filePathUri = if (result.resultCode == Activity.RESULT_OK){
            result!!.data?.data
        } else {
            null
        }
    }

    private fun setupBottomSheetButtons(){
        if (navArgs.currentTodo != null){
            val editTodoText = "Edit todo"
            addEditBtn.text = editTodoText
            setupViews()
        }
        deadlineBtn.setOnClickListener{
            selectDatePicker()
        }
        addImageBtn.setOnClickListener {
            openGalleryForImage()
        }
        addEditBtn.setOnClickListener {
            if (navArgs.currentTodo != null){
//                titleEt.setText(navArgs.currentTodo!!.subject)
//                descriptionEt.setText(navArgs.currentTodo!!.contents)
//                if (navArgs.currentTodo!!.date.isNotEmpty()) {
//                    deadlineBtn.text = navArgs.currentTodo!!.deadlineDate
//                }
                editTodo()
            } else {
                populateTodos()
                val action = AddEditDialogFragmentDirections.actionBottomSheetToTodosFragment(true)
                findNavController().navigate(action)
            }
        }
        notifyCheckBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                setupNotification()
            }
        }
    }

    private fun populateTodos(){
        val currentDate = System.currentTimeMillis()
        if (titleEt.text.isNotEmpty() && descriptionEt.text.isNotEmpty()){
            viewModel.insertHandler(titleEt.text.toString(), descriptionEt.text.toString(), currentDate.toString(), filePathUri.toString(), deadlineBtn.text.toString())
        }
    }

    private fun setupViews(){
        titleEt.setText(navArgs.currentTodo?.subject)
        descriptionEt.setText(navArgs.currentTodo?.contents)
        deadlineBtn.text = navArgs.currentTodo?.deadlineDate
    }

    private fun editTodo(){
        val currentDate = System.currentTimeMillis()
        if (titleEt.text.isNotEmpty() && descriptionEt.text.isNotEmpty()){
            viewModel.updateTodo(Todos(navArgs.currentTodo!!.id, titleEt.text.toString(), descriptionEt.text.toString(), currentDate.toString(), filePathUri.toString(), deadlineBtn.text.toString()))
//            if (isInternetAvailable()){
//                viewModel.updateDataFireStore(navArgs.currentTodo!!.id, titleEt.text.toString(), descriptionEt.text.toString(), currentDate.toString(), filePathUri.toString(), deadlineBtn.text.toString())
//            }
            Log.i("Updates todo", Todos(navArgs.currentTodo!!.id, titleEt.text.toString(), descriptionEt.text.toString(), currentDate.toString(), filePathUri.toString(), deadlineBtn.text.toString()).toString())
            val action = AddEditDialogFragmentDirections.actionBottomSheetToShowTodoFragment(Todos(navArgs.currentTodo!!.id, titleEt.text.toString(), descriptionEt.text.toString(), currentDate.toString(), filePathUri.toString(), deadlineBtn.text.toString()))
            findNavController().navigate(action)
        }
    }

    private fun selectDatePicker() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), R.style.MyDatePicker, { _, year, month, day ->
            TimePickerDialog(requireContext(), R.style.MyDatePicker, { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd HH:mm")
                val localDateTime = LocalDateTime.ofInstant(pickedDateTime.toInstant(), pickedDateTime.timeZone.toZoneId())
                deadlineBtn.text = localDateTime.format(dateFormatter)
                timeInMillis = pickedDateTime.timeInMillis
            }, startHour, startMinute, DateFormat.is24HourFormat(requireContext())).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun setupNotification(){
        val intent = Intent(activity, NotificationReceiver::class.java)
        intent.putExtra("title", titleEt.text.toString())
        intent.putExtra("text", descriptionEt.text.toString())
        val pending =
            PendingIntent.getBroadcast(activity, 42, intent, PendingIntent.FLAG_IMMUTABLE)
        val manager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.i("Time", timeInMillis.toString())
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis - java.time.Duration.ofMinutes(15).toMillis(), pending)
    }

    private fun openGalleryForImage(){
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        resultGetFileLauncher.launch(pickPhoto)
    }

//    private fun isInternetAvailable(): Boolean {
//        return CheckInternetConnection.connectivityStatus(requireContext())
//    }

}