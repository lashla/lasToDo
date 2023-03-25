package com.lasha.lastodo.ui.todos

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.lasha.lastodo.R
import com.lasha.lastodo.data.model.Todo
import com.lasha.lastodo.databinding.FragmentTodoListBinding
import com.lasha.lastodo.domain.utils.CheckInternetConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodosFragment : Fragment() {

    private val navArgs by navArgs<TodosFragmentArgs>()

    private val adapter = TodosRecyclerAdapter()
    private val viewModel by viewModels<TodosViewModel>()

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        initTodosView()
        initViewModel()
        setupBtnListeners()
        setupBackPressAction()
        filterList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            checkPermissions()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        isNewItemAdded()
    }

    private fun initTodosView() {
        binding.recyclerView.run {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = adapter
        }
    }

    private fun setupBackPressAction() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
    }

    private fun setupBtnListeners() {
        binding.run {
            addNewTodoBtn.setOnClickListener {
                showAdditionSheetDialog()
            }
            filterBtn.setOnClickListener {
                val action = TodosFragmentDirections.actionTodosFragmentToFilterDialogFragment()
                findNavController().navigate(action)
            }
            settingsBtn.setOnClickListener {
                val action = TodosFragmentDirections.actionTodosFragmentToProfileFragment()
                findNavController().navigate(action)
            }
        }

        adapter.setOnItemClickListener {
            val action = TodosFragmentDirections.actionTodosFragmentToShowTodoFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun showAdditionSheetDialog() {
        findNavController().navigate(R.id.action_todosFragment_to_bottomSheet)
    }

    private fun isNewItemAdded() {
        if (navArgs.isAddPressed) {
            navArgs.newTodo?.let {
                adapter.insertItem(it)
            }
        }
    }

    private fun filterList() {
        when (navArgs.filterArgs) {
            "time" -> {
                viewModel.getSortedByDate()
            }
            "deadline" -> {
                viewModel.getSortedByDeadline()
            }
            "all" -> {
                viewModel.getAllData(isInternetConnected())
            }
        }
    }

    private fun initViewModel() {
        viewModel.getAllData(isInternetConnected())
        viewModel.todosData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.clearTodos()
                adapter.updateTodoInfo(it as ArrayList<Todo>)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissions(){
        if (!allPermissionsGranted()){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun isInternetConnected(): Boolean{
        return CheckInternetConnection.connectivityStatus(requireContext())
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                android.Manifest.permission.POST_NOTIFICATIONS
            ).apply {
            }.toTypedArray()
    }
}