package com.example.mobile_development_lab_06_verbose_commits

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
    val crimes = mutableListOf<Crime>()
}