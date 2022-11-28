package space.mel.tutorschedule.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@Parcelize

data class Pupil(
    val name: String,
    var grade: Int
): Parcelable, Serializable

