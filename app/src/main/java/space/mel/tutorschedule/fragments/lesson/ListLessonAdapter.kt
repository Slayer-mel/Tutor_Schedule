package space.mel.tutorschedule.fragments.lesson

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import space.mel.tutorschedule.databinding.RvLessonItemBlackBinding
import space.mel.tutorschedule.model.Lesson
import java.text.SimpleDateFormat
import java.util.*

class ListLessonAdapter(val onClick: (Lesson) -> Unit) :
    ListAdapter<Lesson, ListLessonAdapter.LessonViewHolder>(DiffCallback()) {

    inner class LessonViewHolder(private val binding: RvLessonItemBlackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //TODO: Зачастую, оно просит поставить аннотацию "SuppressLint" когда ты
        // используешь устаревший код. Сначала посмотри что оно тебе советует
        // и попробуй сделать нормально. Конкретно тут МОЖНО сделать нормально.
        @SuppressLint("SimpleDateFormat")
        fun bind(lesson: Lesson) {
            binding.apply {
                val simpleDateFormat = SimpleDateFormat("EEEE, dd.MM.yyyy \n HH:mm ")
                val dateString = simpleDateFormat.format(lesson.dataOfLesson)
                //TODO: Ты ж видишь код зачёркнут. Это значит, он устаревший. Сделай так как оно советует.
                tvLessonDate.text = dateString.capitalize(Locale.ROOT)

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