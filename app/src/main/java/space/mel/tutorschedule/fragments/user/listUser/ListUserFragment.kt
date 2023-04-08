package space.mel.tutorschedule.fragments.user.listUser

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.ListFragmentBlackBinding
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.viewmodel.UserViewModel

class ListUserFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: ListFragmentBlackBinding? = null
    private val binding get() = _binding!!

    //private val userViewModel : UserViewModel by activityViewModels()
    private val userViewModel by activityViewModel<UserViewModel>()

    //private val userViewModel by viewModel<UserViewModel>()
    private val listUserAdapter: ListUserAdapter by lazy { ListUserAdapter(::startFullInformation) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListFragmentBlackBinding.inflate(layoutInflater)

        // Recyclerview
        binding.apply {
            recyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = listUserAdapter
                setHasFixedSize(true)
            }
            //swipe delete item from Room DB
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
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
                    val user = listUserAdapter.currentList[viewHolder.adapterPosition]
                    lifecycleScope.launch {
                        userViewModel.deleteUser(user)
                    }
                }
            }).attachToRecyclerView(recyclerview)

        }

        // UserViewModel
        userViewModel.users.observe(viewLifecycleOwner) {
            listUserAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            userViewModel.userEvent.collect { event ->
                when (event) {
                    is UserViewModel.UserEvent.ShowUndoDeleteUserMessage -> {
                        Snackbar.make(
                            requireView(),
                            "Ученик удалён",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction("Отмена") {
                                userViewModel.addUser(event.user)
                            }
                            .setDuration(4500)
                            .setActionTextColor(Color.RED)
                            .show()
                    }
                }
            }
        }

        initListeners()
        return binding.root
    }

    private fun initListeners() {
        with(binding) {
            btnAddNewUser.setOnClickListener {
                findNavController().navigate(R.id.action_listFragmentBlack_to_addFragment)
            }
        }
        //search Query
        binding.search.setOnQueryTextListener(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    private fun startFullInformation(user: User) {
        userViewModel.currentUserEditable.value = user
        findNavController().navigate(R.id.action_listFragmentBlack_to_userFullInformation)
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        userViewModel.searchDatabase(searchQuery).observe(this) { list ->
            list.let { listOfUser ->
                listUserAdapter.submitList(listOfUser)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}