package com.anand.limitless.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "currency"
)
class Currency {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("symbol")
    @Expose
    var symbol: String? = null

}