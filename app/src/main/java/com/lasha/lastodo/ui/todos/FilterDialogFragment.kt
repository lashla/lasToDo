package com.lasha.lastodo.ui.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.lasha.lastodo.R
import kotlinx.android.synthetic.main.filer_dialog.*

class FilterDialogFragment: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(requireContext(), R.layout.filer_dialog, null)
        view.setBackgroundResource(R.drawable.ic_button_background)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFilterButtons()
    }

    private fun setupFilterButtons(){
        filterAllBtn.setOnClickListener {
            val action = FilterDialogFragmentDirections.actionFilterDialogFragmentToTodosFragment(false, "all")
            findNavController().navigate(action)
        }
        filterByTimeBtn.setOnClickListener {
            val action = FilterDialogFragmentDirections.actionFilterDialogFragmentToTodosFragment(false, "time")
            findNavController().navigate(action)
        }
        filterByDeadlineBtn.setOnClickListener {
            val action = FilterDialogFragmentDirections.actionFilterDialogFragmentToTodosFragment(false, "deadline")
            findNavController().navigate(action)
        }
    }
}