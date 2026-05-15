package com.kaushalya.app.data.dao

import androidx.room.*
import com.kaushalya.app.data.entities.WorkerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorker(worker: WorkerEntity): Long

    @Update
    suspend fun updateWorker(worker: WorkerEntity)

    @Delete
    suspend fun deleteWorker(worker: WorkerEntity)

    @Query("SELECT * FROM workers ORDER BY averageRating DESC")
    fun getAllWorkers(): Flow<List<WorkerEntity>>

    @Query("SELECT * FROM workers WHERE id = :workerId LIMIT 1")
    suspend fun getWorkerById(workerId: Int): WorkerEntity?

    @Query("SELECT * FROM workers WHERE userId = :userId LIMIT 1")
    fun getWorkerByUserId(userId: Int): Flow<WorkerEntity?>

    @Query("SELECT * FROM workers WHERE category = :category ORDER BY averageRating DESC")
    fun getWorkersByCategory(category: String): Flow<List<WorkerEntity>>

    @Query("SELECT * FROM workers WHERE name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchWorkers(query: String): Flow<List<WorkerEntity>>

    @Query("SELECT * FROM workers WHERE isFavorite = 1")
    fun getFavoriteWorkers(): Flow<List<WorkerEntity>>

    @Query("SELECT * FROM workers ORDER BY averageRating DESC LIMIT 5")
    fun getTopRatedWorkers(): Flow<List<WorkerEntity>>

    @Query("UPDATE workers SET averageRating = :rating, totalReviews = :total WHERE id = :workerId")
    suspend fun updateRating(workerId: Int, rating: Float, total: Int)

    @Query("UPDATE workers SET isFavorite = :isFavorite WHERE id = :workerId")
    suspend fun updateFavorite(workerId: Int, isFavorite: Boolean)
}
