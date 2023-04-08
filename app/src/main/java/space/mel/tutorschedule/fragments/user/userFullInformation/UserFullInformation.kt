package space.mel.tutorschedule.fragments.user.userFullInformation

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.UserFullInformationFragmentBlackBinding
import space.mel.tutorschedule.viewmodel.UserFullInformationViewModel
import space.mel.tutorschedule.viewmodel.UserViewModel

class UserFullInformation : Fragment() {
    private var _binding: UserFullInformationFragmentBlackBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by activityViewModel<UserViewModel>()
    private val userFullInformationViewModel by viewModel<UserFullInformationViewModel>()
    private val pickImage = 100
    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserFullInformationFragmentBlackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        initObservers()
    }


    private fun initListeners() {
        val userCurrent = userViewModel.currentUserEditable.value
        with(binding) {
            btnMakeLesson.setOnClickListener {
                if (userCurrent != null) {
                    findNavController().navigate(R.id.action_userFullInformation_to_addLesson)
                    //putBundleToSetDateAndTime(userCurrent)
                }
            }
            btnEdit.setOnClickListener {
                if (userCurrent != null) {
                    userViewModel.setCurrentUserEditable(userCurrent)
                    findNavController().navigate(R.id.action_userFullInformation_to_updateFragment)
                }
            }
           /* засетать фото
           imgUser.setOnClickListener {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
            }*/

            btnTelegramWriteMessage.setOnClickListener {
                val telegramIntent = Intent(Intent.ACTION_VIEW)
                //val userTelegramID = "https://t.me/+380669617935"
                //val userTelegramID = "https://t.me/Tetty_S"
                val userTelegramID = userViewModel.currentUserEditable.value?.telegramPupilNumberOrId

                if (userTelegramID.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(), "Добавьте имя пользователя из Telegram",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    with(telegramIntent) {
                        data = Uri.parse("https://t.me/$userTelegramID")
                        setPackage("org.telegram.messenger")
                    }
                    startActivity(telegramIntent)
                }
            }
            btnDeleteUser.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext()).create()
                val view = View.inflate(requireContext(), R.layout.delete_alert_dialog, null)
                val btnCancel = view.findViewById<TextView>(R.id.btnCancel)
                val btnDelete = view.findViewById<TextView>(R.id.btnDelete)
                btnCancel.setOnClickListener {
                    builder.dismiss()
                }
                btnDelete.setOnClickListener {
                    Toast.makeText(
                        requireContext(), "Ученик удален",
                        Toast.LENGTH_LONG
                    ).show()
                    lifecycleScope.launch {
                        deleteUser()
                    }
                    builder.dismiss()
                }
                with(builder) {
                    setView(view)
                    setCancelable(false)
                    show()
                }
            }

            //TEST//////////////////////////////////////////////////////////////////////////////////
            tvName.setOnClickListener {
                val inputText = "+380 66 961 79 35"
                val text = removeWhiteSpace(inputText)
                Toast.makeText(requireContext(), "https://t.me/$text", Toast.LENGTH_LONG).show()
            }

            btnMakeCall.setOnClickListener {
                val inputNumber = userViewModel.currentUserEditable.value?.phonePupilNumber
                val number = removeWhiteSpace(inputNumber.toString())
                val callIntent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts("tel", number, null)
                )
                startActivity(callIntent)
            }

            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_userFullInformation_to_listFragmentBlack)
            }
        }
    }

    private suspend fun deleteUser() {
        val user = userViewModel.currentUserEditable.value
        findNavController().navigate(R.id.action_userFullInformation_to_listFragmentBlack)
        user?.let { userViewModel.deleteUser(it) }
    }


    private fun initObservers() {
        userViewModel.currentUserEditable.observe(viewLifecycleOwner) {user->
            with(binding) {
                Log.d("USERFULL", "user = $user")
                tvName.text = user.name
                tvGrade.text = user.grade.toString()+" класс"
                btnMakeCall.text = user.phonePupilNumber
            }
        }
    }
    /*private fun putBundleToSetDateAndTime(user: User) {
        findNavController().navigate(
            R.id.action_userFullInformation_to_addLesson,
            Bundle().apply {
                putParcelable(
                    "SetDateAndTime",
                    user
                )
            })
    }*/

    private fun removeWhiteSpace(number: String): String {
        return number.replace(" ", "")
    }

    //https://www.tutorialspoint.com/how-to-pick-an-image-from-an-image-gallery-on-android-using-kotlin
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.imgUser.setImageURI(imageUri)
        }
    }*/
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}