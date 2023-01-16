package space.mel.tutorschedule.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.ListFragmentBinding
import space.mel.tutorschedule.viewmodel.UserViewModel

class ListFragment : Fragment() {
    private lateinit var listBinding: ListFragmentBinding
    private lateinit var userViewModel: UserViewModel
    private val myAdapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listBinding = ListFragmentBinding.inflate(layoutInflater)

        // Recyclerview
        listBinding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        listBinding.recyclerview.adapter = myAdapter

        // UserViewModel
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            myAdapter.setItem(user)
        })

        //swipe delete item from Room DB
        val itemSwipe = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.bindingAdapterPosition
                val deletedUser = myAdapter.userList[pos]
                userViewModel.deleteUser(deletedUser)
                view?.let {
                    Snackbar.make(it, "Ученик удалён", Snackbar.LENGTH_LONG).apply {
                        setAction("Отмена") {
                            userViewModel.addUser(deletedUser)
                        }
                        show()
                    }
                }
            }
        }
        ItemTouchHelper(itemSwipe).apply {
            attachToRecyclerView(listBinding.recyclerview)
        }

        listBinding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        listBinding.btnToCalendar.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_calendarFragment)
        }


        listBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    searchDatabase(query)
                }
                return true
            }
        })
        return listBinding.root
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        userViewModel.searchDatabase(searchQuery).observe(this) { list ->
            list.let { listOfUser ->
                myAdapter.setItem(listOfUser)
            }
        }
    }
}