package space.mel.tutorschedule.fragments.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.UserFullInformationFragmentBlackBinding
import space.mel.tutorschedule.utils.AlertDialogProvider
import space.mel.tutorschedule.utils.Constants
import space.mel.tutorschedule.viewmodel.UserFullInformationViewModel
import space.mel.tutorschedule.viewmodel.UserViewModel

class UserFullInformationFragment : Fragment() {
    private var _binding: UserFullInformationFragmentBlackBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by activityViewModel<UserViewModel>()
    private val userFullInformationViewModel by viewModel<UserFullInformationViewModel>()

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
        with(binding) {
            btnMakeLesson.setOnClickListener {
                val userCurrent = userViewModel.currentUserEditable.value
                if (userCurrent != null) {
                    findNavController().navigate(R.id.action_userFullInformation_to_addLesson)
                }
            }
            btnEdit.setOnClickListener {
                val userCurrent = userViewModel.currentUserEditable.value
                if (userCurrent != null) {
                    userViewModel.setCurrentUserEditable(userCurrent)
                    findNavController().navigate(R.id.action_userFullInformation_to_updateFragment)
                }
            }
            /* засетать фото
            imgUser.setOnClickListener {
                 val gallery =
                     Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                 startActivityForResult(gallery, Constants.PICK_iMAGE)
             }*/

            btnTelegramWriteMessage.setOnClickListener {
                val userTelegramID =userViewModel.currentUserEditable.value?.telegramPupilNumberOrId
                if (userTelegramID.isNullOrEmpty()) {
                    showToast(R.string.user_full_information_fragment_add_name_toast)
                } else { messageToTelegramIntent(userTelegramID) }
            }
            btnDeleteUser.setOnClickListener {
                AlertDialogProvider.createAlertInstance(
                    requireContext(),
                    R.layout.delete_user_alert_dialog,
                    onDelete = {
                        showToast(R.string.common_snack_bar_user_delete)
                        deleteUser()
                    }
                ).show()
            }

            btnMakeCall.setOnClickListener {
                val inputNumber = userViewModel.currentUserEditable.value?.phonePupilNumber
                val number = removeWhiteSpace(inputNumber.toString())
                val callIntent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts(Constants.TEL, number, null)
                )
                startActivity(callIntent)
            }

            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_userFullInformation_to_listFragmentBlack)
            }
            btnAllLessonsList.setOnClickListener {
                findNavController().navigate(R.id.action_userFullInformation_to_listLessonFragment)
            }
        }
    }

    //TODO: Переименуй функцию, название говно. Конечная
    // цель - навигация не создание интента, а навигация в телегу
    private fun messageToTelegramIntent(inputUserTelegramID:String?) {
        val telegramIntent = Intent(Intent.ACTION_VIEW)
        with(telegramIntent) {
            val userTelegramID = removeAt(telegramId = inputUserTelegramID)
            data = Uri.parse("${Constants.TELEGRAM_HTTP}$userTelegramID")
            setPackage(Constants.TELEGRAM_PACKAGE)
        }
        startActivity(telegramIntent)
    }

    private fun showToast(toastInt:Int) {
        Toast.makeText(
            requireContext(),
            toastInt,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun deleteUser() {
        val user = userViewModel.currentUserEditable.value
        user?.let { userViewModel.deleteUser(it) }
        findNavController().navigate(R.id.action_userFullInformation_to_listFragmentBlack)
    }


    private fun initObservers() {
        userViewModel.currentUserEditable.observe(viewLifecycleOwner) { user ->
            with(binding) {
                //TODO: Я смотрю у тебя логов дохуя. Используй дебаггер. В будущем,
                // когда у тебя будет большой проект и он будет собираться по 15 минут,
                // тебе будет очень неудобно и долго бедажить в помощью Log.d
                Log.d("USERFULL", "user = $user")
                tvName.text = user.name
                tvGrade.text = "${user.grade} ${getString(R.string.common_grade_lowercase)}"
                Log.d("LOGSLOGS", "${tvGrade.text}")
                btnMakeCall.text = user.phonePupilNumber
            }
        }
    }

    private fun removeAt(telegramId: String?): String {
        return telegramId?.replace("@", "") ?: ""
    }

    private fun removeWhiteSpace(number: String): String {
        return number.replace(" ", "")
    }

    //https://www.tutorialspoint.com/how-to-pick-an-image-from-an-image-gallery-on-android-using-kotlin
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == Constants.PICK_iMAGE) {
            imageUri = data?.data
            binding.imgUser.setImageURI(imageUri)
        }
    }*/
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}