package com.anand.limitless.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.anand.limitless.util.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "state"
)
class StateName {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @SerializedName("name")
    @Expose
    var name: String?=null
    @SerializedName("topLevelDomain")
    @Expose
    @TypeConverters(Converters::class)
    var topLevelDomain: List<String>? = listOf()
    @SerializedName("alpha2Code")
    @Expose
    var alpha2Code: String?=null
    @SerializedName("alpha3Code")
    @Expose
    var alpha3Code: String?=null
    @SerializedName("callingCodes")
    @Expose
    @TypeConverters(Converters::class)
    var callingCodes: List<String>?=null
    @SerializedName("capital")
    @Expose
    var capital: String?=null
    @SerializedName("altSpellings")
    @Expose
    @TypeConverters(Converters::class)
    var altSpellings: List<String>? = listOf()
    @SerializedName("region")
    @Expose
    var region: String? = null
    @SerializedName("subregion")
    @Expose
    var subregion: String?=null
    @SerializedName("population")
    @Expose
    var population: Int?=null
    @SerializedName("score")
    @Expose
    var score: Int?=null
    @SerializedName("latlng")
    @Expose
    @TypeConverters(ConvertersDouble::class)
    var latlng: List<Double>? = listOf()
    @SerializedName("demonym")
    @Expose
    var demonym: String?=null
    @SerializedName("area")
    @Expose
    var area: Double?=null
    @SerializedName("gini")
    @Expose
    @TypeConverters(ConvertersAnyModel::class)
    var gini: Any?=null
    @SerializedName("timezones")
    @Expose
    @TypeConverters(Converters::class)
    var timezones: List<String> = listOf()
    @SerializedName("borders")
    @Expose
    @TypeConverters(ConvertersAny::class)
    var borders: List<Any> = listOf()
    @SerializedName("nativeName")
    @Expose
    var nativeName: String?=null
    @SerializedName("numericCode")
    @Expose
    var numericCode: String?=null
    @SerializedName("currencies")
    @Expose
    @TypeConverters(ConvertersCurrency::class)
    var currencies: List<Currency> = listOf()
    @SerializedName("languages")
    @Expose
    @TypeConverters(ConvertersLanguage::class)
    var languages: List<Language> = listOf()
    @SerializedName("translations")
    @Expose
    @TypeConverters(ConvertersTranslationsModel::class)
    var translations: Translations?=null
    @SerializedName("flag")
    @Expose
    var flag: String?=null
    @SerializedName("regionalBlocs")
    @Expose
    @TypeConverters(ConvertersRegionalBloc::class)
    var regionalBlocs: List<RegionalBloc>  = listOf()
    @SerializedName("cioc")
    @Expose
    var cioc: String?=null
    // to be consistent w/ changing backend order we need to keep a data like this
    var indexInResponse: Int = -1
}
