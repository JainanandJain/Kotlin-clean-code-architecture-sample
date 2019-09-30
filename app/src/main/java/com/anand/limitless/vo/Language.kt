package com.anand.limitless.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "language"
)
class Language {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("iso639_1")
    @Expose
    var iso6391: String? = null
    @SerializedName("iso639_2")
    @Expose
    var iso6392: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("nativeName")
    @Expose
    var nativeName: String? = null

}
