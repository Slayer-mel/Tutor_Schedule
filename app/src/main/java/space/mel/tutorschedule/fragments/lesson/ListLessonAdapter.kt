package space.mel.tutorschedule.fragments.lesson

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import space.mel.tutorschedule.databinding.RvLessonItemBlackBinding
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.utils.stringCapitalize
import java.text.SimpleDateFormat
import java.util.*

class ListLessonAdapter(val onClick: (Lesson) -> Unit) :
    ListAdapter<Lesson, ListLessonAdapter.LessonViewHolder>(DiffCallback()) {

    inner class LessonViewHolder(private val binding: RvLessonItemBlackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson) {
            binding.apply {
                //TODO: Для всех конвертаций времени используй DateTimeHelper. Ты ж не зря его создавал
                val simpleDateFormat = SimpleDateFormat("EEEE, dd.MM.yyyy \n HH:mm ", Locale.getDefault())
                val dateString = simpleDateFormat.format(lesson.dataOfLesson)
                tvLessonDate.text = stringCapitalize(dateString)
                //TODO: Удоли
                //tvLessonDate.text = dateString.capitalize(Locale.ROOT)

                root.setOnClickListener {
                    onClick.invoke(lesson)
                }
            }
        }
    }

    //создает recyclerView Holder и инициализирует views для списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        return LessonViewHolder(
            RvLessonItemBlackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    //связывает views с содержимым
    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val currentLesson = getItem(position)
        holder.bind(currentLesson)
    }

    class DiffCallback : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
        }
    }
}