package com.aayush.todolistapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.aayush.todolistapp.database.TaskDatabase
import com.aayush.todolistapp.databinding.ActivityMainBinding
import com.aayush.todolistapp.repository.TaskRepository
import com.aayush.todolistapp.viewmodel.TaskViewModel
import com.aayush.todolistapp.viewmodel.TaskViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var tasksViewModel: TaskViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                tasksViewModel.isLoading.value
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
    }

    private fun setUpViewModel() {
        val taskRepository = TaskRepository(TaskDatabase(this))
        val viewModelProviderFactory = TaskViewModelFactory(application, taskRepository)
        tasksViewModel = ViewModelProvider(this, viewModelProviderFactory)[TaskViewModel::class.java]
    }

    @Deprecated("Deprecated in Java", ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity"))
    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val currentDestination = navController.currentDestination

        if (currentDestination?.id == R.id.homeFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

}
