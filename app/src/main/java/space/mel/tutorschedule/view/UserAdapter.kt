package space.mel.tutorschedule.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import space.mel.tutorschedule.R
import space.mel.tutorschedule.model.PupilData

class UserAdapter(
    val context: Context,
    val pupilList:ArrayList<PupilData>
    ) : RecyclerView.Adapter<UserAdapter.PupilViewHolder>()
{

    inner class PupilViewHolder(view:View):RecyclerView.ViewHolder(view){
        var name:TextView
        var grade:TextView
        private var mMenus:ImageView

        init {
            name = view.findViewById(R.id.mTitle)
            grade = view.findViewById(R.id.mSubTitle)
            mMenus = view.findViewById(R.id.mMenus)
            mMenus.setOnClickListener { popupMenus(it) }
        }

        private fun popupMenus(view:View) {
            val position = pupilList[adapterPosition]
            val popupMenus = PopupMenu(context,view)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editText->{
                        val v = LayoutInflater.from(context).inflate(R.layout.add_item,null)
                        val name = v.findViewById<EditText>(R.id.userName)
                        val number = v.findViewById<EditText>(R.id.userNo)
                        AlertDialog.Builder(context)
                            .setView(v)
                            .setPositiveButton("Добавить"){ //Ok
                                    dialog,_->
                                position.name = name.text.toString()
                                position.grade = number.text.toString()
                                notifyDataSetChanged()
                                Toast.makeText(context,"Информация изменена",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("Отмена"){ //Cancel
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
                    R.id.delete->{
                        /**set delete*/
                        AlertDialog.Builder(context)
                            .setTitle("Удалить")
                            .setIcon(R.drawable.ic_warning)
                            .setMessage("Вы точно хотите удалить?")
                            .setPositiveButton("Да"){
                                    dialog,_->
                                pupilList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(context,"Ученик удален",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("Нет"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else-> true
                }
            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PupilViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.list_item,parent,false)
        return PupilViewHolder(v)
    }

    override fun onBindViewHolder(holder: PupilViewHolder, position: Int) {
        val newList = pupilList[position]
        holder.name.text = newList.name
        holder.grade.text = newList.grade
    }

    override fun getItemCount(): Int {
        return  pupilList.size
    }
}
