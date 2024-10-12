package com.example.mobile_development_lab_06_verbose_commits

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Crime(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false)
//                 var requiresPolice: Boolean = false)
//data class Crime(val id: UUID = UUID.randomUUID(),
//                 var title: String = "",
//                 var date: Date = Date(),
//                 var isSolved: Boolean = false,
//                 var requiresPolice: Boolean = false