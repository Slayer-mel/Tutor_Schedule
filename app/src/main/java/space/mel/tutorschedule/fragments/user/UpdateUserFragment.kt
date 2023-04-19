package space.mel.tutorschedule.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.UpdateUserFragmentBlackBinding
import space.mel.tutorschedule.model.Parent
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.viewmodel.UserViewModel

class UpdateUserFragment : Fragment() {
    private var _binding: UpdateUserFragmentBlackBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by activityViewModel<UserViewModel>()
    //private val userViewModel : UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UpdateUserFragmentBlackBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        with(binding){
            btnOk.setOnClickListener {
                updateItem()
            }
            btnBack.setOnClickListener {
                goBack()
            }
            btnCancel.setOnClickListener {
                goBack()
            }
        }

    }

    private fun goBack() {
        findNavController().navigate(R.id.action_updateFragment_to_userFullInformation)
    }

    private fun initObservers() {
        userViewModel.currentUserEditable.observe(viewLifecycleOwner) { user ->
            //val currentUser = userViewModel.currentUserEditable.value
            with(binding) {
                edtUpdateName.setText(user.name)
                edtUpdateGrade.setText(user.grade.toString())
                edtPhonePupilNumber.setText(user.phonePupilNumber)
                edtTelegramNumber.setText(user.telegramPupilNumberOrId)
                edtMomOrDad.setText(user.parent?.momDad)
                edtMomOrDadName.setText(user.parent?.name)
                edtPhoneParentNumber.setText(user.parent?.phoneParentNumber)
                edtTelegramParentNumber.setText(user.parent?.telegramParentId)
            }
        }
    }

    private fun updateItem() {
        val userCurrent = userViewModel.currentUserEditable.value
        val namePupil = binding.edtUpdateName.text.toString()
        val grade = binding.edtUpdateGrade.text.toString()
        val phonePupilNumber = binding.edtPhonePupilNumber.text.toString()
        val telegramNumberOrId = binding.edtTelegramNumber.text.toString()
        val momOrDad = binding.edtMomOrDad.text.toString()
        val nameMomOrDad = binding.edtMomOrDadName.text.toString()
        val phoneParentNumber = binding.edtPhoneParentNumber.text.toString()
        val telegramParentPhoneOrId = binding.edtTelegramParentNumber.text.toString()

        if (userCurrent != null && namePupil.isNotEmpty() && grade.isNotEmpty()) {
            val id = userCurrent.id
            // Create User Object
            val updatedUser = User(
                id = id,
                name = namePupil,
                grade = grade.toInt(),
                phonePupilNumber = phonePupilNumber,
                telegramPupilNumberOrId = telegramNumberOrId,
                parent = Parent(
                    momDad = momOrDad,
                    name = nameMomOrDad,
                    phoneParentNumber = phoneParentNumber,
                    telegramParentId = telegramParentPhoneOrId
                )
            )
            // Update Current User
            with(userViewModel){
                updateUser(updatedUser)
                currentUserEditable.value = updatedUser
            }
            Toast.makeText(requireContext(), "Данные изменены!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_userFullInformation)
        } else {
            Toast.makeText(requireContext(), "Заполните все данные", Toast.LENGTH_SHORT)
                .show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}
