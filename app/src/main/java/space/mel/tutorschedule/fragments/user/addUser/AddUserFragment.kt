package space.mel.tutorschedule.fragments.user.addUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.AddFragmentBlackBinding
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.viewmodel.UserViewModel

class AddUserFragment : Fragment() {
    private var _binding: AddFragmentBlackBinding? = null
    private val binding get() = _binding!!
    //private val userViewModel by viewModel<UserViewModel>()
    private val userViewModel by activityViewModel<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddFragmentBlackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
    }

    private fun initObservers() {
        with(binding){
            btnOk.setOnClickListener {
                insertDataToDatabase()
            }
            btnCancel.setOnClickListener {
                goBack()
            }
            btnBack.setOnClickListener {
                goBack()
            }
        }
    }

    private fun goBack() {
        findNavController().navigate(R.id.action_addFragment_to_listFragmentBlack)
    }

    private fun insertDataToDatabase() {
        val name = binding.edtAddName.text.toString()
        val grade = binding.edtAddGrade.text

        if (name.isEmpty() ||  grade.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Заполните Имя и класс", Toast.LENGTH_LONG)
                .show()
        } else {
            // Create User Object
            val user = User(
                name = name,
                grade = grade.toString().toInt()
            )
            // Add Data to Database
            userViewModel.addUser(user)
            Toast.makeText(requireContext(), "Ученик добавлен", Toast.LENGTH_LONG).show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragmentBlack)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}