package com.anand.limitless.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Class to convert arraylist into string
 */
class ConvertersDouble {
    @TypeConverter
    fun stringToList(data: String?): List<Double>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Double>>() {

        }.type

        return Gson().fromJson<List<Double>>(data, listType)
    }

    @TypeConverter
    fun listToString(list: List<Double>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}