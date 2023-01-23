package space.mel.tutorschedule.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import space.mel.tutorschedule.databinding.CustomRowBinding
import space.mel.tutorschedule.model.User

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()

    fun setItem(user: List<User>) {
        this.userList = user
        notifyDataSetChanged()
    }

    class MyViewHolder(val myBinding: CustomRowBinding) : RecyclerView.ViewHolder(myBinding.root){
        fun setData(user: User) {
            myBinding.tvFirstName.text = user.firstName
            myBinding.tvGrade.text = user.grade.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.setData(currentItem)
        holder.myBinding.rowLayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }
}