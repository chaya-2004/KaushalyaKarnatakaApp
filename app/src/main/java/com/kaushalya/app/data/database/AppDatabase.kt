package com.kaushalya.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kaushalya.app.data.dao.*
import com.kaushalya.app.data.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Main Room Database class. Singleton pattern ensures one instance throughout app lifecycle.
 */
@Database(
    entities = [
        UserEntity::class,
        WorkerEntity::class,
        ServiceEntity::class,
        ReviewEntity::class,
        PortfolioImageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun workerDao(): WorkerDao
    abstract fun serviceDao(): ServiceDao
    abstract fun reviewDao(): ReviewDao
    abstract fun portfolioDao(): PortfolioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kaushalya_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Prepopulate the database with sample data on first creation.
     */
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateSampleData(database)
                }
            }
        }

        private suspend fun populateSampleData(db: AppDatabase) {
            // Sample workers for demonstration
            val workers = listOf(
                WorkerEntity(
                    userId = 0,
                    name = "Raju Electricals",
                    phone = "9876543210",
                    category = "Electrician",
                    address = "Jayanagar, Bengaluru",
                    experience = 8,
                    aboutMe = "Expert in house wiring, panel installation, and electrical repairs. Available 24/7 for emergencies.",
                    averageRating = 4.5f,
                    totalReviews = 24
                ),
                WorkerEntity(
                    userId = 0,
                    name = "Suresh Plumbing Works",
                    phone = "9845001234",
                    category = "Plumber",
                    address = "Koramangala, Bengaluru",
                    experience = 12,
                    aboutMe = "Specialist in pipe fitting, leak repair, bathroom fittings, and drainage systems.",
                    averageRating = 4.2f,
                    totalReviews = 18
                ),
                WorkerEntity(
                    userId = 0,
                    name = "Kiran Carpentry",
                    phone = "9900112233",
                    category = "Carpenter",
                    address = "Indiranagar, Bengaluru",
                    experience = 15,
                    aboutMe = "Custom furniture, wardrobes, modular kitchen, and door/window work at affordable prices.",
                    averageRating = 4.8f,
                    totalReviews = 35
                ),
                WorkerEntity(
                    userId = 0,
                    name = "Manjunath Painters",
                    phone = "9845678901",
                    category = "Painter",
                    address = "Rajajinagar, Bengaluru",
                    experience = 6,
                    aboutMe = "Interior and exterior painting, texture work, waterproofing, and wallpaper installation.",
                    averageRating = 4.0f,
                    totalReviews = 12
                ),
                WorkerEntity(
                    userId = 0,
                    name = "Venkatesh Auto Service",
                    phone = "9731234567",
                    category = "Mechanic",
                    address = "Whitefield, Bengaluru",
                    experience = 10,
                    aboutMe = "Two-wheeler and four-wheeler repair, servicing, AC repair, and denting/painting.",
                    averageRating = 4.6f,
                    totalReviews = 42
                )
            )
            workers.forEach { db.workerDao().insertWorker(it) }

            // Sample services
            val services = listOf(
                ServiceEntity(workerId = 1, serviceName = "House Wiring", description = "Complete house electrical wiring for new or old constructions", price = 5000.0, priceType = "Starting from"),
                ServiceEntity(workerId = 1, serviceName = "Fan Installation", description = "Ceiling fan / exhaust fan installation with wiring", price = 300.0, priceType = "Fixed"),
                ServiceEntity(workerId = 2, serviceName = "Pipe Leak Repair", description = "Fix water pipe leakages quickly and efficiently", price = 500.0, priceType = "Starting from"),
                ServiceEntity(workerId = 2, serviceName = "Bathroom Fitting", description = "Complete bathroom fitting including WC, shower, and taps", price = 3000.0, priceType = "Starting from"),
                ServiceEntity(workerId = 3, serviceName = "Wardrobe Making", description = "Custom wooden wardrobe with sliding doors", price = 15000.0, priceType = "Starting from"),
                ServiceEntity(workerId = 4, serviceName = "Interior Painting", description = "Room painting with primer and 2 coats of paint", price = 8000.0, priceType = "Starting from"),
                ServiceEntity(workerId = 5, serviceName = "Bike Service", description = "Full two-wheeler service with oil change and filter", price = 800.0, priceType = "Fixed")
            )
            services.forEach { db.serviceDao().insertService(it) }

            // Sample reviews
            val reviews = listOf(
                ReviewEntity(workerId = 1, reviewerName = "Anitha R.", rating = 5f, comment = "Excellent work! Fixed all wiring issues quickly."),
                ReviewEntity(workerId = 1, reviewerName = "Prakash M.", rating = 4f, comment = "Good work, came on time and did clean job."),
                ReviewEntity(workerId = 3, reviewerName = "Meena S.", rating = 5f, comment = "Beautiful wardrobe! Very professional and clean work."),
                ReviewEntity(workerId = 5, reviewerName = "Rahul K.", rating = 4f, comment = "My bike runs perfectly now. Reasonable price.")
            )
            reviews.forEach { db.reviewDao().insertReview(it) }
        }
    }
}
