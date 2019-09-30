package com.anand.limitless.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Class to convert arraylist into string
 */

class Converters {
    @TypeConverter
    fun stringToList(data: String?): List<String>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<String>>() {

        }.type

        return Gson().fromJson<List<String>>(data, listType)
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}