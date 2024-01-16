package com.aayush.todolistapp.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.aayush.todolistapp.MainActivity
import com.aayush.todolistapp.R
import com.aayush.todolistapp.databinding.FragmentNewTaskBinding
import com.aayush.todolistapp.model.Task
import com.aayush.todolistapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewTaskFragment : Fragment(R.layout.fragment_new_task) {

    private var _binding : FragmentNewTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var tasksViewModel : TaskViewModel

    private lateinit var mView: View

    private var selectedDate = Calendar.getInstance()
    private var selectedTime = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksViewModel = (activity as MainActivity).tasksViewModel

        mView = view

        binding.addTaskBtnNew.setOnClickListener{
            saveTask(view)
        }

        binding.datePicker.setOnClickListener {
            showDatePickerDialog()

        }
        binding.timePicker.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun saveTask(view: View) {
        val taskTitle = binding.etTaskTitleNew.text.toString().trim()

        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        if (taskTitle.isNotEmpty()){
            val task = Task(0,taskTitle,false,formatDate(selectedDate),formatTime(selectedTime))

            tasksViewModel.addTask(task)
            Toast.makeText(mView.context, "Task Added Successfully",Toast.LENGTH_SHORT).show()

            val userName = sharedPreferences.getString("userName", "")

            val bundle = Bundle().apply {
                putString("userName",userName)
            }

            view.findNavController().navigate(R.id.action_newTaskFragment_to_homeFragment,bundle)
        } else{
            Toast.makeText(mView.context,"Please Enter Task",Toast.LENGTH_LONG).show()
        }
    }

    private fun formatDate(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun formatTime(calendar: Calendar): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(calendar.time)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate.set(year, month, dayOfMonth)
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
            },
            selectedTime.get(Calendar.HOUR_OF_DAY),
            selectedTime.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}