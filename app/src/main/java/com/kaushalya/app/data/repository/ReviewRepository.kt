package com.kaushalya.app.data.repository

import com.kaushalya.app.data.dao.ReviewDao
import com.kaushalya.app.data.entities.ReviewEntity
import kotlinx.coroutines.flow.Flow

class ReviewRepository(private val reviewDao: ReviewDao) {

    fun getReviewsForWorker(workerId: Int): Flow<List<ReviewEntity>> =
        reviewDao.getReviewsForWorker(workerId)

    suspend fun insertReview(review: ReviewEntity): Long = reviewDao.insertReview(review)

    suspend fun deleteReview(review: ReviewEntity) = reviewDao.deleteReview(review)

    suspend fun getAverageRating(workerId: Int): Float =
        reviewDao.getAverageRating(workerId) ?: 0f

    suspend fun getReviewCount(workerId: Int): Int = reviewDao.getReviewCount(workerId)
}
