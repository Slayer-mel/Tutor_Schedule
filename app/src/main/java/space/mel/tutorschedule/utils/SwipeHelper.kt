package space.mel.tutorschedule.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeHelper(
    dragDirs: Int = 0,
    swipeDirs: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
    private val swipe: (viewHolderAdapterPosition: Int) -> Unit

) : ItemTouchHelper.SimpleCallback(
    dragDirs, swipeDirs
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    //тело выполнять во фрагменте, передавать в конструктор класса SwipeHelper как лямбда
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val viewHolderAdapterPosition = viewHolder.adapterPosition
        swipe(viewHolderAdapterPosition)
    }
}