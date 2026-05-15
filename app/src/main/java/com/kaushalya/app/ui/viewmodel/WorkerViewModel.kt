package com.kaushalya.app.ui.viewmodel

import androidx.lifecycle.*
import com.kaushalya.app.data.entities.WorkerEntity
import com.kaushalya.app.data.repository.ReviewRepository
import com.kaushalya.app.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class WorkerViewModel(
    private val workerRepository: WorkerRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    // Search & filter state
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("All")

    // All workers flow with live filter
    val allWorkers: LiveData<List<WorkerEntity>> = workerRepository.getAllWorkers().asLiveData()

    val topRatedWorkers: LiveData<List<WorkerEntity>> =
        workerRepository.getTopRatedWorkers().asLiveData()

    val favoriteWorkers: LiveData<List<WorkerEntity>> =
        workerRepository.getFavoriteWorkers().asLiveData()

    private val _filteredWorkers = MutableLiveData<List<WorkerEntity>>()
    val filteredWorkers: LiveData<List<WorkerEntity>> = _filteredWorkers

    private val _operationResult = MutableLiveData<OperationResult>()
    val operationResult: LiveData<OperationResult> = _operationResult

    fun searchWorkers(query: String) {
        viewModelScope.launch {
            workerRepository.searchWorkers(query).collect { workers ->
                _filteredWorkers.value = workers
            }
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            if (category == "All") {
                workerRepository.getAllWorkers().collect { workers ->
                    _filteredWorkers.value = workers
                }
            } else {
                workerRepository.getWorkersByCategory(category).collect { workers ->
                    _filteredWorkers.value = workers
                }
            }
        }
    }

    fun insertWorker(worker: WorkerEntity) {
        viewModelScope.launch {
            val id = workerRepository.insertWorker(worker)
            _operationResult.value = OperationResult.Success("Worker profile created!", id.toInt())
        }
    }

    fun updateWorker(worker: WorkerEntity) {
        viewModelScope.launch {
            workerRepository.updateWorker(worker)
            _operationResult.value = OperationResult.Success("Worker profile updated!", worker.id)
        }
    }

    fun deleteWorker(worker: WorkerEntity) {
        viewModelScope.launch {
            workerRepository.deleteWorker(worker)
            _operationResult.value = OperationResult.Deleted
        }
    }

    fun toggleFavorite(workerId: Int, currentState: Boolean) {
        viewModelScope.launch {
            workerRepository.toggleFavorite(workerId, !currentState)
        }
    }

    fun refreshRating(workerId: Int) {
        viewModelScope.launch {
            val avg = reviewRepository.getAverageRating(workerId)
            val count = reviewRepository.getReviewCount(workerId)
            workerRepository.updateRating(workerId, avg, count)
        }
    }

    fun getWorkerByUserId(userId: Int): LiveData<WorkerEntity?> =
        workerRepository.getWorkerByUserId(userId).asLiveData()

    sealed class OperationResult {
        data class Success(val message: String, val id: Int = 0) : OperationResult()
        object Deleted : OperationResult()
        data class Error(val message: String) : OperationResult()
    }
}
