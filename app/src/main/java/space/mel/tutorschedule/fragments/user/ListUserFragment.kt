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
import space.mel.tutorschedule.utils.SwipeHelper
import space.mel.tutorschedule.viewmodel.UserViewModel

//TODO: Не наследуй фрагмент от SearchView.OnQueryTextListener. Создай отдельный класс
class ListUserFragment : Fragment(), SearchView.OnQueryTextListener {
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

        //TODO: Какого хрена это всё делает в OnCreateView? Перемести в onViewCreated
        // и разнеси по функциям. У тебя тут и сетап recyclerView, и observe, и collect
        // Recyclerview
        binding.apply {
            recyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = listUserAdapter
                setHasFixedSize(true)
            }
            //swipe delete item from Room DB
            ItemTouchHelper(
                SwipeHelper{ viewHolderAdapterPosition->
                    val user = listUserAdapter.currentList[viewHolderAdapterPosition]
                    lifecycleScope.launch {
                        userViewModel.deleteUser(user)
                    }
                }
            ).attachToRecyclerView(recyclerview)
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
                            //TODO: В строковые ресурсы
                            "Ученик удалён",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            //TODO: В строковые ресурсы
                            .setAction("Отмена") {
                                userViewModel.addUser(event.user)
                            }
                            //TODO: Magic number
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
        //TODO: Нахрена тебе знаки процента в запросе?
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