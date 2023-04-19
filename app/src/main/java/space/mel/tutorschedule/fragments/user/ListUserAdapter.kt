package space.mel.tutorschedule.fragments.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import space.mel.tutorschedule.databinding.RvUserItemBlackBinding
import space.mel.tutorschedule.model.User

class ListUserAdapter(val onClick: (User) -> Unit) :
    ListAdapter<User, ListUserAdapter.UserViewHolder>(DiffCallback()) {

    inner class UserViewHolder(private val binding: RvUserItemBlackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                tvName.text = user.name
                tvGrade.text = user.grade.toString()
                root.setOnClickListener {
                    onClick.invoke(user)
                }
            }
        }
    }

    //создает recyclerView Holder и инициализирует views для списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            RvUserItemBlackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    //связывает views с содержимым
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = getItem(position)
        holder.bind(currentUser)
    }

    class DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}