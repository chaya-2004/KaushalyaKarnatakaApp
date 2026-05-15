package com.kaushalya.app.data.repository

import com.kaushalya.app.data.dao.PortfolioDao
import com.kaushalya.app.data.entities.PortfolioImageEntity
import kotlinx.coroutines.flow.Flow

class PortfolioRepository(private val portfolioDao: PortfolioDao) {

    fun getImagesForWorker(workerId: Int): Flow<List<PortfolioImageEntity>> =
        portfolioDao.getImagesForWorker(workerId)

    suspend fun insertImage(image: PortfolioImageEntity): Long = portfolioDao.insertImage(image)

    suspend fun deleteImage(image: PortfolioImageEntity) = portfolioDao.deleteImage(image)

    suspend fun deleteAllImagesForWorker(workerId: Int) =
        portfolioDao.deleteAllImagesForWorker(workerId)
}
