package space.mel.tutorschedule.fragments.user

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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.ListUserFragmentBlackBinding
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.utils.Constants
import space.mel.tutorschedule.utils.SwipeHelper
import space.mel.tutorschedule.viewmodel.UserViewModel

class ListUserFragment : Fragment()/*,
    SearchView.OnQueryTextListener*/ {
    private var _binding: ListUserFragmentBlackBinding? = null
    private val binding get() = _binding!!

    //private val userViewModel : UserViewModel by activityViewModels()
    private val userViewModel by activityViewModel<UserViewModel>()

    //private val userViewModel by viewModel<UserViewModel>()
    private val listUserAdapter: ListUserAdapter by lazy { ListUserAdapter(::startFullInformation) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListUserFragmentBlackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        initAdapter()
        initObservers()
    }

    private fun initObservers() {
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
                            R.string.common_snack_bar_user_delete,
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction(R.string.common_btn_cancel_gray_background) {
                                userViewModel.addUser(event.user)
                            }
                            .setDuration(Constants.DURATION_TIME_DELETE_MESSAGE)
                            .setActionTextColor(Color.RED)
                            .show()
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        binding.apply {
            recyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = listUserAdapter
                setHasFixedSize(true)
            }
            //swipe delete item from Room DB
            ItemTouchHelper(
                SwipeHelper { viewHolderAdapterPosition ->
                    val user = listUserAdapter.currentList[viewHolderAdapterPosition]
                    lifecycleScope.launch {
                        userViewModel.deleteUser(user)
                    }
                }
            ).attachToRecyclerView(recyclerview)
        }
    }

    private fun initListeners() {
        with(binding) {
            btnAddNewUser.setOnClickListener {
                findNavController().navigate(R.id.action_listFragmentBlack_to_addFragment)
            }
        }
        //search Query
        searchQuery()
        //binding.search.setOnQueryTextListener(this)
    }

    private fun startFullInformation(user: User) {
        userViewModel.currentUserEditable.value = user
        findNavController().navigate(R.id.action_listFragmentBlack_to_userFullInformation)
    }

    private fun searchDatabase(query: String) {
            userViewModel.searchDatabase(query).observe(this) { list ->
            list.let { listOfUser ->
                listUserAdapter.submitList(listOfUser)
            }
        }
    }

    private fun searchQuery() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}