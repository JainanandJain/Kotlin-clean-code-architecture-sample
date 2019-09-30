package com.anand.limitless.util

import androidx.room.TypeConverter
import com.anand.limitless.vo.Translations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Class to convert arraylist into string
 */
class ConvertersTranslationsModel {
    @TypeConverter
    fun stringToAny(data: String?): Translations {
        if (data == null) {
            return Translations()
        }

        val listType = object : TypeToken<Translations>() {

        }.type

        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun listToString(list: Translations): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}