package com.example.documentreader

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.documentreader.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener {
            val navController = findNavController(R.id.fragment)
            when(it.itemId){
                R.id.all_files -> navController.navigate(R.id.allDocsFragment)
                R.id.fav_files -> navController.navigate(R.id.favoriteFragment)
            }
            true
        }
        binding.bottomNav.setOnItemReselectedListener {

        }

    }
}



