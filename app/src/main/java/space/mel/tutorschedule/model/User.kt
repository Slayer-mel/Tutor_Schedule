package space.mel.tutorschedule.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val grade: Int,
    val phonePupilNumber: String? = null,
    val telegramPupilNumberOrId: String?= null,
    @SerializedName("parent")
    var parent: Parent? = null
) : Parcelable, Serializable

@Parcelize
@Entity
data class Parent(
    val momDad: String?,
    val name: String?,
    val phoneParentNumber: String?,
    val telegramParentId: String?
) : Parcelable


class ParentTypeConverter() {
    @TypeConverter
    fun toParent(value: String): Parent? {
        val type = object : TypeToken<Parent?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toString(value : Parent?) : String? {
        return Gson().toJson(value)
    }
}