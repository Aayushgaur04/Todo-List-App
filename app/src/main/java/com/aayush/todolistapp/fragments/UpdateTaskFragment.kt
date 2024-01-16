package com.aayush.todolistapp.fragments

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs
import com.aayush.todolistapp.MainActivity
import com.aayush.todolistapp.R
import com.aayush.todolistapp.databinding.FragmentUpdateTaskBinding
import com.aayush.todolistapp.model.Task
import com.aayush.todolistapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateTaskFragment : Fragment(R.layout.fragment_update_task) {

    private var _binding : FragmentUpdateTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var tasksViewModel : TaskViewModel

    private lateinit var currentTask: Task
    private val args: UpdateTaskFragmentArgs by navArgs()

    private var selectedDate = Calendar.getInstance()
    private var selectedTime = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksViewModel = (activity as MainActivity).tasksViewModel
        currentTask = args.task!!

        binding.etTaskTitleUpdate.setText(currentTask.taskTitle)

        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        binding.addTaskBtnUpdate.setOnClickListener{
            val title = binding.etTaskTitleUpdate.text.toString().trim()

            if (title.isNotEmpty()) {
                val task = Task(currentTask.id,title,false,formatDate(selectedDate),formatTime(selectedTime))
                tasksViewModel.updateTask(task)

                val userName = sharedPreferences.getString("userName", "")

                val bundle = Bundle().apply {
                    putString("userName",userName)
                }
                view.findNavController().navigate(R.id.action_updateTaskFragment_to_homeFragment,bundle)
            } else {
                Toast.makeText(context,"Please Enter the Task", Toast.LENGTH_LONG).show()
            }
        }

        binding.datePicker.setOnClickListener {
            showDatePickerDialog()
        }

        binding.timePicker.setOnClickListener {
            showTimePickerDialog()
        }

        binding.delTaskBtnUpdate.setOnClickListener{
            deleteTask()
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

    private fun deleteTask() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Task")
            setMessage("You want to delete this task?")
            setPositiveButton("Delete"){_,_ ->
                tasksViewModel.deleteTask(currentTask)
                view?.findNavController()?.navigate(R.id.action_updateTaskFragment_to_homeFragment)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}