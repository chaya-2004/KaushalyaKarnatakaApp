package com.kaushalya.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaushalya.app.data.repository.*

/**
 * Factory for creating ViewModels with constructor arguments.
 */
class ViewModelFactory(
    private val userRepository: UserRepository? = null,
    private val workerRepository: WorkerRepository? = null,
    private val serviceRepository: ServiceRepository? = null,
    private val reviewRepository: ReviewRepository? = null,
    private val portfolioRepository: PortfolioRepository? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(userRepository!!) as T

            modelClass.isAssignableFrom(WorkerViewModel::class.java) ->
                WorkerViewModel(workerRepository!!, reviewRepository!!) as T

            modelClass.isAssignableFrom(ServiceViewModel::class.java) ->
                ServiceViewModel(serviceRepository!!) as T

            modelClass.isAssignableFrom(ReviewViewModel::class.java) ->
                ReviewViewModel(reviewRepository!!, workerRepository!!) as T

            modelClass.isAssignableFrom(PortfolioViewModel::class.java) ->
                PortfolioViewModel(portfolioRepository!!) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
