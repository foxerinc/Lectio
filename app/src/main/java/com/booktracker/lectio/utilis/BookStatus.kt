package com.booktracker.lectio.utilis

import androidx.room.TypeConverter

enum class BookStatusType(val value: String) {
    WANT_TO_READ(WANT_TO_READ_VALUE),
    CURRENTLY_READING(CURRENTLY_READING_VALUE),
    FINISHED_READING(FINISHED_READING_VALUE);

    companion object {
        fun fromValue(value: String): BookStatusType {
            return entries.find { it.value == value } ?: WANT_TO_READ
        }
    }
}


class BookStatusTypeConverter {

    @TypeConverter
    fun fromStatus(status: BookStatusType): String {
        return status.value
    }

    @TypeConverter
    fun toStatus(value: String): BookStatusType {
        return BookStatusType.fromValue(value)
    }
}