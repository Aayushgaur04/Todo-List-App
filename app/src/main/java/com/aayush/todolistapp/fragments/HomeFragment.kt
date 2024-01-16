package com.aayush.todolistapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aayush.todolistapp.MainActivity
import com.aayush.todolistapp.R
import com.aayush.todolistapp.adapter.TaskAdapter
import com.aayush.todolistapp.databinding.FragmentHomeBinding
import com.aayush.todolistapp.model.Task
import com.aayush.todolistapp.viewmodel.TaskViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var tasksViewModel : TaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksViewModel = (activity as MainActivity).tasksViewModel


        setUpRecyclerView()

        binding.floatingActionButton.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_newTaskFragment)
        }

        val userName = arguments?.getString("userName")
        Log.d("HomeFragment", "Received userName: $userName")

        if (!userName.isNullOrEmpty()) {
            binding.userName.text = userName
        } else {
            Log.e("HomeFragment", "userName is null or empty")
        }

        tasksViewModel.taskCompletionStatusUpdated.observe(viewLifecycleOwner) {
            taskAdapter.notifyDataSetChanged()
        }
    }

    private fun setUpRecyclerView() {
        taskAdapter = TaskAdapter(tasksViewModel)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter = taskAdapter
        }

        activity.let {
            tasksViewModel.getAllTasks().observe(
                viewLifecycleOwner
            ) { task ->
                taskAdapter.differ.submitList(task)
                updateUI(task)
            }
        }

    }

    private fun updateUI(task: List<Task>?) {
        if (task != null) {
            if (task.isNotEmpty()){
                binding.noTaskAvailable.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }else{
                binding.noTaskAvailable.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}