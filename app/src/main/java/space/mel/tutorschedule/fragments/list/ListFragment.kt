package space.mel.tutorschedule.fragments.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.ListFragmentBinding
import space.mel.tutorschedule.viewmodel.UserViewModel

class ListFragment : Fragment(), SearchView.OnQueryTextListener {
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
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            myAdapter.setItem(user)
        })

        listBinding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        return listBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!=null){
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query!=null){
            searchDatabase(query)
        }
        return true
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