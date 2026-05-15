package com.kaushalya.app.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents an app user (Worker or Customer).
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullName: String,
    val phone: String,
    val email: String,
    val password: String,
    val userType: String, // "Worker" or "Customer"
    val createdAt: Long = System.currentTimeMillis()
)
