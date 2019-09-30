package com.anand.limitless.util

import androidx.room.TypeConverter
import com.anand.limitless.vo.Language
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Class to convert arraylist into string
 */
class ConvertersLanguage {
    @TypeConverter
    fun stringToList(data: String?): List<Language>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Language>>() {

        }.type

        return Gson().fromJson<List<Language>>(data, listType)
    }

    @TypeConverter
    fun listToString(list: List<Language>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}