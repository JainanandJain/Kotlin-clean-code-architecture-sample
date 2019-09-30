package com.anand.limitless.vo

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "translations"
)

class Translations() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("de")
    @Expose
    var de: String? = null
    @SerializedName("es")
    @Expose
    var es: String? = null
    @SerializedName("fr")
    @Expose
    var fr: String? = null
    @SerializedName("ja")
    @Expose
    var ja: String? = null
    @SerializedName("it")
    @Expose
    var it: String? = null
    @SerializedName("br")
    @Expose
    var br: String? = null
    @SerializedName("pt")
    @Expose
    var pt: String? = null
    @SerializedName("nl")
    @Expose
    var nl: String? = null
    @SerializedName("hr")
    @Expose
    var hr: String? = null
    @SerializedName("fa")
    @Expose
    var fa: String? = null

    constructor(parcel: Parcel) : this() {
        de = parcel.readString()
        es = parcel.readString()
        fr = parcel.readString()
        ja = parcel.readString()
        it = parcel.readString()
        br = parcel.readString()
        pt = parcel.readString()
        nl = parcel.readString()
        hr = parcel.readString()
        fa = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(de)
        parcel.writeString(es)
        parcel.writeString(fr)
        parcel.writeString(ja)
        parcel.writeString(it)
        parcel.writeString(br)
        parcel.writeString(pt)
        parcel.writeString(nl)
        parcel.writeString(hr)
        parcel.writeString(fa)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Translations> {
        override fun createFromParcel(parcel: Parcel): Translations {
            return Translations(parcel)
        }

        override fun newArray(size: Int): Array<Translations?> {
            return arrayOfNulls(size)
        }
    }

}
