package com.kaushalya.app.data.repository

import com.kaushalya.app.data.dao.WorkerDao
import com.kaushalya.app.data.entities.WorkerEntity
import kotlinx.coroutines.flow.Flow

class WorkerRepository(private val workerDao: WorkerDao) {

    fun getAllWorkers(): Flow<List<WorkerEntity>> = workerDao.getAllWorkers()

    fun getTopRatedWorkers(): Flow<List<WorkerEntity>> = workerDao.getTopRatedWorkers()

    fun searchWorkers(query: String): Flow<List<WorkerEntity>> = workerDao.searchWorkers(query)

    fun getWorkersByCategory(category: String): Flow<List<WorkerEntity>> =
        workerDao.getWorkersByCategory(category)

    fun getFavoriteWorkers(): Flow<List<WorkerEntity>> = workerDao.getFavoriteWorkers()

    fun getWorkerByUserId(userId: Int): Flow<WorkerEntity?> = workerDao.getWorkerByUserId(userId)

    suspend fun getWorkerById(workerId: Int): WorkerEntity? = workerDao.getWorkerById(workerId)

    suspend fun insertWorker(worker: WorkerEntity): Long = workerDao.insertWorker(worker)

    suspend fun updateWorker(worker: WorkerEntity) = workerDao.updateWorker(worker)

    suspend fun deleteWorker(worker: WorkerEntity) = workerDao.deleteWorker(worker)

    suspend fun toggleFavorite(workerId: Int, isFavorite: Boolean) =
        workerDao.updateFavorite(workerId, isFavorite)

    suspend fun updateRating(workerId: Int, rating: Float, total: Int) =
        workerDao.updateRating(workerId, rating, total)
}
