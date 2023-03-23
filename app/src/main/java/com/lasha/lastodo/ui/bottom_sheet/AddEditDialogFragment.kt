package com.lasha.lastodo.ui.bottom_sheet

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
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
import com.lasha.lastodo.data.model.Todo
import com.lasha.lastodo.databinding.AddEditDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class AddEditDialogFragment : BottomSheetDialogFragment() {

    private val navArgs by navArgs<AddEditDialogFragmentArgs>()

    private var _binding: AddEditDialogBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var viewModel: AddEditViewModel
    private var filePathUri: Uri? = null
    private var timeInMillis: Long = 0
    private var todo = Todo()

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
    ): View {
        _binding = AddEditDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[AddEditViewModel::class.java]
        setupBottomSheetButtons()
        super.onViewCreated(view, savedInstanceState)
    }

    private var resultGetFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            filePathUri = if (result.resultCode == Activity.RESULT_OK) {
                result!!.data?.data
            } else {
                null
            }
        }

    private fun setupBottomSheetButtons() {
        binding.apply {
            if (navArgs.currentTodo != null) {
                val editTodoText = "Edit todo"
                addEditBtn.text = editTodoText
                setupViews()
            }
            deadlineBtn.setOnClickListener {
                selectDatePicker()
            }
            addImageBtn.setOnClickListener {
                openGalleryForImage()
            }
            addEditBtn.setOnClickListener {
                if (navArgs.currentTodo != null) {
                    editTodo()
                } else {
                    populateTodos()
                    val action =
                        AddEditDialogFragmentDirections.actionBottomSheetToTodosFragment(newTodo = todo, isAddPressed = true)
                    findNavController().navigate(action)
                }
            }
            notifyCheckBtn.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    setupNotification()
                }
            }
        }
    }

    private fun populateTodos() {
        val currentDate = System.currentTimeMillis()
        binding.apply {
            if (titleEt.text.isNotEmpty() && descriptionEt.text.isNotEmpty()) {
                viewModel.insertHandler(
                    titleEt.text.toString(),
                    descriptionEt.text.toString(),
                    currentDate.toString(),
                    filePathUri.toString(),
                    deadlineBtn.text.toString()
                )
                todo = Todo(
                    0,
                    titleEt.text.toString(),
                    descriptionEt.text.toString(),
                    currentDate.toString(),
                    filePathUri.toString(),
                    deadlineBtn.text.toString()
                )
            }
        }
    }

    private fun setupViews() {
        binding.apply {
            titleEt.setText(navArgs.currentTodo?.subject)
            descriptionEt.setText(navArgs.currentTodo?.contents)
            deadlineBtn.text = navArgs.currentTodo?.deadlineDate
        }
    }

    private fun editTodo() {
        val currentDate = System.currentTimeMillis()
        binding.apply {
            if (titleEt.text.isNotEmpty() && descriptionEt.text.isNotEmpty()) {
                viewModel.updateTodo(
                    Todo(
                        navArgs.currentTodo!!.id,
                        titleEt.text.toString(),
                        descriptionEt.text.toString(),
                        currentDate.toString(),
                        filePathUri.toString(),
                        deadlineBtn.text.toString()
                    )
                )
                val action = AddEditDialogFragmentDirections.actionBottomSheetToShowTodoFragment(
                    Todo(
                        navArgs.currentTodo!!.id,
                        titleEt.text.toString(),
                        descriptionEt.text.toString(),
                        currentDate.toString(),
                        filePathUri.toString(),
                        deadlineBtn.text.toString()
                    )
                )
                findNavController().navigate(action)
            }
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
                val localDateTime = LocalDateTime.ofInstant(
                    pickedDateTime.toInstant(),
                    pickedDateTime.timeZone.toZoneId()
                )
                binding.deadlineBtn.text = localDateTime.format(dateFormatter)
                timeInMillis = pickedDateTime.timeInMillis
            }, startHour, startMinute, DateFormat.is24HourFormat(requireContext())).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun setupNotification() {
        val intent = Intent(activity, NotificationReceiver::class.java)
        val pending =
            PendingIntent.getBroadcast(activity, 42, intent, PendingIntent.FLAG_IMMUTABLE)
        val manager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis - java.time.Duration.ofMinutes(15).toMillis(),
            pending
        )
    }

    private fun openGalleryForImage() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        resultGetFileLauncher.launch(pickPhoto)
    }
}