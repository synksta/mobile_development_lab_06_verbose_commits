package com.example.mobile_development_lab_06_verbose_commits

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_development_lab_06_verbose_commits.databinding.ActivityMainBinding
import com.example.mobile_development_lab_06_verbose_commits.CrimeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        supportFragmentManager.findFragmentById(binding.fragmentContainer.id)?.let {
            // Фрагмент уже существует, ничего не делаем
        } ?: run {
            // Если фрагмент не найден, создаем новый
            val fragment = CrimeFragment()
            supportFragmentManager
                .beginTransaction()
                .add(binding.fragmentContainer.id, fragment)
                .commit()

        }
    }
}
