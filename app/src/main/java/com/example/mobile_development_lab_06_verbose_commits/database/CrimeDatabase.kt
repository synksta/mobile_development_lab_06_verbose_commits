package com.example.mobile_development_lab_06_verbose_commits.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mobile_development_lab_06_verbose_commits.Crime

@Database(
    entities = [ Crime::class ],
    version=1
)

@TypeConverters(
    CrimeTypeConverters::class
)

abstract class CrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): CrimeDao

}