package com.kaushalya.app.ui.viewmodel

import androidx.lifecycle.*
import com.kaushalya.app.data.entities.ReviewEntity
import com.kaushalya.app.data.repository.ReviewRepository
import com.kaushalya.app.data.repository.WorkerRepository
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val workerRepository: WorkerRepository
) : ViewModel() {

    private val _workerId = MutableLiveData<Int>()

    val reviews: LiveData<List<ReviewEntity>> = _workerId.switchMap { id ->
        reviewRepository.getReviewsForWorker(id).asLiveData()
    }

    private val _operationResult = MutableLiveData<String>()
    val operationResult: LiveData<String> = _operationResult

    fun loadReviewsForWorker(workerId: Int) {
        _workerId.value = workerId
    }

    fun submitReview(workerId: Int, reviewerName: String, rating: Float, comment: String) {
        viewModelScope.launch {
            if (rating == 0f) {
                _operationResult.value = "Please select a rating"
                return@launch
            }
            val review = ReviewEntity(
                workerId = workerId,
                reviewerName = reviewerName,
                rating = rating,
                comment = comment
            )
            reviewRepository.insertReview(review)

            // Recalculate and update average rating on worker
            val avg = reviewRepository.getAverageRating(workerId)
            val count = reviewRepository.getReviewCount(workerId)
            workerRepository.updateRating(workerId, avg, count)

            _operationResult.value = "Review submitted successfully!"
        }
    }

    fun deleteReview(review: ReviewEntity, workerId: Int) {
        viewModelScope.launch {
            reviewRepository.deleteReview(review)
            val avg = reviewRepository.getAverageRating(workerId)
            val count = reviewRepository.getReviewCount(workerId)
            workerRepository.updateRating(workerId, avg, count)
            _operationResult.value = "Review deleted"
        }
    }
}
