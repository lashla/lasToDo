package com.lasha.lastodo.ui.todos

import android.R.attr
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lasha.lastodo.R
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.ui.bottom_sheet.AddEditDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_edit_dialog.*
import kotlinx.android.synthetic.main.todos_fragment.*
import java.time.LocalDateTime
import java.util.*


@AndroidEntryPoint
class TodosFragment: Fragment(R.layout.todos_fragment) {

    private val adapter = TodosRecyclerAdapter()
    private lateinit var viewModel: TodosViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TodosViewModel::class.java]
        initViewModel()
        initTodosView()
        setupBtnListeners()
        checkPermissions()
    }

    private fun initTodosView(){
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    private fun setupBtnListeners(){
        addNewTodoBtn.setOnClickListener {
            showAdditionSheetDialog()
        }
        adapter.setOnItemClickListener {
            val action = TodosFragmentDirections.actionTodosFragmentToShowTodoFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun showAdditionSheetDialog(){
        
    }

    private fun initViewModel(){
        viewModel.todosData.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                adapter.updateTodoInfo(it as ArrayList<Todos>)
                Log.i("ViewTodos", "Todos inserted")
            }
        }
    }

    private fun checkPermissions(){
        if (!allPermissionsGranted()){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ).apply {
            }.toTypedArray()
    }
}