package com.kaushalya.app.data.repository

import com.kaushalya.app.data.dao.UserDao
import com.kaushalya.app.data.entities.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: UserEntity): Result<Long> {
        return try {
            val existing = userDao.getUserByEmail(user.email)
            if (existing != null) {
                Result.failure(Exception("Email already registered"))
            } else {
                val id = userDao.insertUser(user)
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        return userDao.loginUser(email, password)
    }

    suspend fun getUserById(userId: Int): UserEntity? {
        return userDao.getUserById(userId)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }
}
