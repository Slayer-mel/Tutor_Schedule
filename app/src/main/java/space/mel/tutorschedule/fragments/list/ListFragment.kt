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
import androidx.recyclerview.widget.LinearLayoutManager
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.ListFragmentBinding
import space.mel.tutorschedule.viewmodel.UserViewModel

class ListFragment : Fragment(){
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

        listBinding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        listBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query!=null){
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