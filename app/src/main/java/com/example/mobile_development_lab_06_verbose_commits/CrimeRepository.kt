package com.example.mobile_development_lab_06_verbose_commits

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.mobile_development_lab_06_verbose_commits.database.CrimeDatabase
import java.util.Date
import java.util.UUID
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            CrimeDatabase::class.java,
            DATABASE_NAME
        )
            .build()

    private val crimeDao = database.crimeDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun fillDatabaseWithSampleData() {
        executor.execute {
            // Создаем несколько примеров преступлений
            val sampleCrimes = listOf(
                Crime(title = "Первое преступление", date = Date(), isSolved = false),
                Crime(title = "Второе преступление", date = Date(), isSolved = true),
                Crime(title = "Третье преступление", date = Date(), isSolved = false)
            )
            // Добавляем их в базу данных
            sampleCrimes.forEach { crime ->
                crimeDao.addCrime(crime)
            }
        }
    }

    fun getCrimes(): LiveData<List<Crime>> =
        crimeDao.getCrimes()

    fun getCrime(id: UUID): LiveData<Crime?> =
        crimeDao.getCrime(id)

    fun updateCrime(crime: Crime) {
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime) {
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
//                INSTANCE!!.fillDatabaseWithSampleData()
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}
