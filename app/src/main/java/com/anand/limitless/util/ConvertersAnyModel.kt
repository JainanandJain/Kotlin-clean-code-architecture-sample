package com.anand.limitless.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Class to convert arraylist into string
 */
class ConvertersAnyModel {
    @TypeConverter
    fun stringToAny(data: String?): Any? {
        if (data == null) {
            return Any()
        }

        val listType = object : TypeToken<Any>() {

        }.type

        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun listToString(list: Any?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}