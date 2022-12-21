package space.mel.tutorschedule.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.ListFragmentBinding
import space.mel.tutorschedule.viewmodel.UserViewModel

class ListFragment : Fragment() {
    private lateinit var listBinding : ListFragmentBinding
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listBinding = ListFragmentBinding.inflate(layoutInflater)

        // Recyclerview
        val adapter = ListAdapter()
        val recyclerView = listBinding.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // UserViewModel
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mUserViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            adapter.setItem(user)
        })

        listBinding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        return listBinding.root
    }
}