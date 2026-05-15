package com.kaushalya.app.data.dao

import androidx.room.*
import com.kaushalya.app.data.entities.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Update
    suspend fun updateReview(review: ReviewEntity)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE workerId = :workerId ORDER BY createdAt DESC")
    fun getReviewsForWorker(workerId: Int): Flow<List<ReviewEntity>>

    @Query("SELECT AVG(rating) FROM reviews WHERE workerId = :workerId")
    suspend fun getAverageRating(workerId: Int): Float?

    @Query("SELECT COUNT(*) FROM reviews WHERE workerId = :workerId")
    suspend fun getReviewCount(workerId: Int): Int
}
