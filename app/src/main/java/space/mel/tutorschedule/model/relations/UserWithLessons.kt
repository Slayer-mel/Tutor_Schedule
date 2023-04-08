package space.mel.tutorschedule.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.model.User

data class UserWithLessons(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val lessons: List<Lesson>
)
