package com.lasha.lastodo.ui.todos

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.lasha.lastodo.R
import com.lasha.lastodo.databinding.DialogFilterBinding

class FilterDialogFragment : DialogFragment() {

    private var _binding: DialogFilterBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFilterButtons()
    }

    private fun setupFilterButtons() {
        binding.run {
            filterAllBtn.setOnClickListener {
                filterByTimeBtn.setTextColor(com.google.android.material.R.attr.colorOnPrimary)
                filterByDeadlineBtn.setTextColor(com.google.android.material.R.attr.colorOnPrimary)
                filterAllBtn.setTextColor(com.google.android.material.R.attr.colorSecondary)
                val action =
                    FilterDialogFragmentDirections.actionFilterDialogFragmentToTodosFragment(
                        isAddPressed = false,
                        filterArgs = "all",
                        newTodo = null
                    )
                findNavController().navigate(action)
            }
            filterByTimeBtn.setOnClickListener {
                filterByTimeBtn.setTextColor(com.google.android.material.R.attr.colorSecondary)
                filterByDeadlineBtn.setTextColor(com.google.android.material.R.attr.colorOnPrimary)
                filterAllBtn.setTextColor(com.google.android.material.R.attr.colorOnPrimary)

                val action =
                    FilterDialogFragmentDirections.actionFilterDialogFragmentToTodosFragment(
                        newTodo = null,
                        isAddPressed = false,
                        filterArgs = "time"
                    )
                findNavController().navigate(action)
            }
            filterByDeadlineBtn.setOnClickListener {
                filterByDeadlineBtn.setTextColor(com.google.android.material.R.attr.colorSecondary)
                filterAllBtn.setTextColor(com.google.android.material.R.attr.colorOnPrimary)
                filterByTimeBtn.setTextColor(com.google.android.material.R.attr.colorOnPrimary)
                val action =
                    FilterDialogFragmentDirections.actionFilterDialogFragmentToTodosFragment(
                        newTodo = null,
                        isAddPressed = false,
                        filterArgs = "deadline"
                    )
                findNavController().navigate(action)
            }
        }
    }
}