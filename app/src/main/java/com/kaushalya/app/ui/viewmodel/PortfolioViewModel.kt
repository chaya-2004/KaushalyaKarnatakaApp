package com.kaushalya.app.ui.viewmodel

import androidx.lifecycle.*
import com.kaushalya.app.data.entities.PortfolioImageEntity
import com.kaushalya.app.data.repository.PortfolioRepository
import kotlinx.coroutines.launch

class PortfolioViewModel(private val portfolioRepository: PortfolioRepository) : ViewModel() {

    private val _workerId = MutableLiveData<Int>()

    val portfolioImages: LiveData<List<PortfolioImageEntity>> = _workerId.switchMap { id ->
        portfolioRepository.getImagesForWorker(id).asLiveData()
    }

    private val _operationResult = MutableLiveData<String>()
    val operationResult: LiveData<String> = _operationResult

    fun loadImagesForWorker(workerId: Int) {
        _workerId.value = workerId
    }

    fun addImage(workerId: Int, imageUri: String, caption: String = "") {
        viewModelScope.launch {
            val image = PortfolioImageEntity(workerId = workerId, imageUri = imageUri, caption = caption)
            portfolioRepository.insertImage(image)
            _operationResult.value = "Photo added to portfolio"
        }
    }

    fun deleteImage(image: PortfolioImageEntity) {
        viewModelScope.launch {
            portfolioRepository.deleteImage(image)
            _operationResult.value = "Photo removed"
        }
    }
}
