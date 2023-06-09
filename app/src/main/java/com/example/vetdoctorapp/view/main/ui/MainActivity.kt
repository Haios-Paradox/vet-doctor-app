package com.example.vetdoctorapp.view.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.vetdoctorapp.R
import com.example.vetdoctorapp.controller.main.MainViewModel
import com.example.vetdoctorapp.databinding.ActivityMainBinding
import com.example.vetdoctorapp.view.auth.ui.AuthActivity

class MainActivity : AppCompatActivity() {
    //TODO: Make Payment Feature
    //TODO: The Flow First
    //TODO: Then figure out the backend
    //TODO: Then the XML
    //TODO: Setup Fragment and all that jazz


    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)

        mainViewModel.msg.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }

        mainViewModel.userData.observe(this){
            if(it!=null && it.available==true){
                Navigation.findNavController(
                    this, binding.fragmentContainerView2.id
                ).navigate(R.id.homeFragment)
            }
            else
                Navigation.findNavController(
                    this, binding.fragmentContainerView2.id
                ).navigate(R.id.settingFragment)
        }

        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_profile -> {
                    Navigation.findNavController(
                        this, binding.fragmentContainerView2.id
                    ).navigate(R.id.profileFragment)
                    true
                }
                R.id.navigation_doctors ->{
                    if(mainViewModel.userData.value?.available==true)
                        Navigation.findNavController(
                            this, binding.fragmentContainerView2.id
                        ).navigate(R.id.homeFragment)
                    else
                        Navigation.findNavController(
                            this, binding.fragmentContainerView2.id
                        ).navigate(R.id.settingFragment)
                    true
                }
                R.id.navigation_history -> {
                    Navigation.findNavController(
                        this, binding.fragmentContainerView2.id
                    ).navigate(R.id.appointmentListFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                mainViewModel.logout()
                startActivity(
                    Intent(this, AuthActivity::class.java).apply {
                        addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    }
                )
            }

        }
        return true
    }
}