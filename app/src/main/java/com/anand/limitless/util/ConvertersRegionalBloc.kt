package com.anand.limitless.util

import androidx.room.TypeConverter
import com.anand.limitless.vo.RegionalBloc
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Class to convert arraylist into string
 */
class ConvertersRegionalBloc {
    @TypeConverter
    fun stringToList(data: String?): List<RegionalBloc>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<RegionalBloc>>() {

        }.type

        return Gson().fromJson<List<RegionalBloc>>(data, listType)
    }

    @TypeConverter
    fun listToString(list: List<RegionalBloc>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}