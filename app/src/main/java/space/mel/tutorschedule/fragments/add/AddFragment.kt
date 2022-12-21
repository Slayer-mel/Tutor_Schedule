package space.mel.tutorschedule.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.AddFragmentBinding
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.viewmodel.UserViewModel

class AddFragment : Fragment() {
    private lateinit var addBinding: AddFragmentBinding
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addBinding = AddFragmentBinding.inflate(layoutInflater)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        addBinding.addBtn.setOnClickListener {
            insertDataToDatabase()
        }
        return addBinding.root
    }

    private fun insertDataToDatabase() {
        val name = addBinding.edtAddName.text.toString()
        val grade = addBinding.edtAddGrade.text

        if (name.isEmpty() ||  grade.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG)
                .show()
        } else {
            // Create User Object
            val user = User(0, name, Integer.parseInt(grade.toString()))
            // Add Data to Database
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
    }
}