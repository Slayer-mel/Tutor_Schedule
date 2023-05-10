package space.mel.tutorschedule.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.AddUserFragmentBlackBinding
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.viewmodel.AddUserViewModel

class AddUserFragment : Fragment() {
    private var _binding: AddUserFragmentBlackBinding? = null
    private val binding get() = _binding!!
    //private val userViewModel by viewModel<UserViewModel>()
    private val addUserViewModel by activityViewModel<AddUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddUserFragmentBlackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        with(addUserViewModel){
            isButtonOkValid.observe(viewLifecycleOwner){ isValid ->
                binding.btnOk.isEnabled = isValid
            }
        }
    }

    private fun initListeners() {
        with(binding){
            edtAddName.addTextChangedListener{ name ->
                with(addUserViewModel){
                    setName(name.toString())
                    isFieldsValid()
                }
            }
            edtAddGrade.addTextChangedListener{ grade ->
                with(addUserViewModel){
                    setGrade(grade.toString())
                    isFieldsValid()
                }
            }
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

    //saveData
    private fun insertDataToDatabase() {
        val name = binding.edtAddName.text.toString()
        val grade = binding.edtAddGrade.text.toString()

            // Create User Object
            val user = User(
                name = name,
                grade = grade.toInt()
            )
            // Add Data to Database
            addUserViewModel.addUser(user)
            Toast.makeText(requireContext(), R.string.common_snack_bar_user_added, Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragmentBlack)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}