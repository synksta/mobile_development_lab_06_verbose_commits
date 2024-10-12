package com.example.mobile_development_lab_06_verbose_commits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
    private val _crimeList = MutableLiveData<List<Crime>>()
    val crimeListLiveData: LiveData<List<Crime>> get() = _crimeList

    init {
        val crimes = mutableListOf<Crime>()
        for (i in 0 until 100) {
            val crime = Crime(
                title = "Crime #$i",
                isSolved = i % 2 == 0,
            )
            crimes.add(crime)
        }
        _crimeList.value = crimes // Устанавливаем список преступлений
    }
}