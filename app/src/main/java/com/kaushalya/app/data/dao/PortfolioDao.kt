package com.kaushalya.app.data.dao

import androidx.room.*
import com.kaushalya.app.data.entities.PortfolioImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: PortfolioImageEntity): Long

    @Delete
    suspend fun deleteImage(image: PortfolioImageEntity)

    @Query("SELECT * FROM portfolio_images WHERE workerId = :workerId ORDER BY createdAt DESC")
    fun getImagesForWorker(workerId: Int): Flow<List<PortfolioImageEntity>>

    @Query("DELETE FROM portfolio_images WHERE workerId = :workerId")
    suspend fun deleteAllImagesForWorker(workerId: Int)
}
