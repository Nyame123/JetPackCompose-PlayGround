package com.bismark.core.database.utils

import androidx.room.TypeConverter
import com.bismark.core.model.NewsResourceType
import com.bismark.core.model.asNewsResourceType
import kotlinx.datetime.Instant

class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()
}

class NewsResourceTypeConverter {
    @TypeConverter
    fun newsResourceTypeToString(value: NewsResourceType?): String? =
        value?.let(NewsResourceType::serializedName)

    @TypeConverter
    fun stringToNewsResourceType(serializedName: String?): NewsResourceType =
        serializedName.asNewsResourceType()
}
