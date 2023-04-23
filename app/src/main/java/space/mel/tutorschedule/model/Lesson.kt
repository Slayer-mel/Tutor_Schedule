package space.mel.tutorschedule.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val topic: String? = null,
    val mark: Int? = null,
    val dataOfLesson: Long? = null,
    val homeWork: String? = null,
    val userId: List<Int>? = null
) : Parcelable

class UserIdTypeConverter() {
    @TypeConverter
    fun toUserId(value: String): List<Int>? {
        val type = object : TypeToken<List<Int>?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toString(list : List<Int>?) : String? {
        return Gson().toJson(list)
    }
}