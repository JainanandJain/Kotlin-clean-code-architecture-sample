package com.anand.limitless.util

import androidx.room.TypeConverter
import com.anand.limitless.vo.Currency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Class to convert arraylist into string
 */
class ConvertersCurrency {
    @TypeConverter
    fun fromString(value: String): List<Currency>? {
        val listType = object : TypeToken<ArrayList<Currency>>() {

        }.type
        return Gson().fromJson<ArrayList<Currency>>(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<Currency>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}