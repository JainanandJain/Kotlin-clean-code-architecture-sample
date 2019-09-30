package com.anand.limitless.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "regional_bloc"
)
class RegionalBloc {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("acronym")
    @Expose
    var acronym: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("otherAcronyms")
    @Expose
    var otherAcronyms: List<Any>? = null
    @SerializedName("otherNames")
    @Expose
    var otherNames: List<Any>? = null

}
