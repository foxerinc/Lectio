package com.booktracker.lectio.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.booktracker.lectio.data.datasource.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(reviewEntity: ReviewEntity): Long

    @Update
    suspend fun updateReview(reviewEntity: ReviewEntity)

    @Delete
    suspend fun deleteReview(reviewEntity: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE book_id = :bookId ORDER BY created_at DESC")
    fun getReviewsByBook(bookId: Int): Flow<List<ReviewEntity>>
}