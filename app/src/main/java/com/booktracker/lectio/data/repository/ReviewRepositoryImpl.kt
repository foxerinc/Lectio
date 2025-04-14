package com.booktracker.lectio.data.repository

import com.booktracker.lectio.data.datasource.local.database.LectioDatabase
import com.booktracker.lectio.data.datasource.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepositoryImpl @Inject constructor(
    private val appDatabase: LectioDatabase
) {

    fun getReviewsByBook(bookId: Int): Flow<List<ReviewEntity>> =
        appDatabase.reviewDao.getReviewsByBook(bookId)

    suspend fun insertReview(reviewEntity: ReviewEntity): Long = appDatabase.reviewDao.insertReview(reviewEntity)

    suspend fun updateReview(reviewEntity: ReviewEntity) = appDatabase.reviewDao.updateReview(reviewEntity)

    suspend fun deleteReview(reviewEntity: ReviewEntity) = appDatabase.reviewDao.deleteReview(reviewEntity)
}