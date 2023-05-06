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
        //TODO: Логически неверно объявлять переменную здесь. В данный момент ты сохраняешь
        // юзера в переменную при вызове функции "initListeners". Далее лисенеры будут работать
        // только с этой переменной. Если при каком-то сценарии юзер внутри currentUserEditable
        // поменяется, лисенеры продолжат работать со старым юзером, который был сохранён в переменную
        // userCurrent при вызове функции initListeners
        val userCurrent = userViewModel.currentUserEditable.value
        with(binding) {
            btnMakeLesson.setOnClickListener {
                if (userCurrent != null) {
                    findNavController().navigate(R.id.action_userFullInformation_to_addLesson)
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
                 startActivityForResult(gallery, Constants.PICK_iMAGE)
             }*/

            //TODO: Много логики в одном блоке. Раскидай по функциям
            btnTelegramWriteMessage.setOnClickListener {
                val telegramIntent = Intent(Intent.ACTION_VIEW)
                //val userTelegramID = "https://t.me/+380669617935"
                //val userTelegramID = "https://t.me/Tetty_S"
                val userTelegramID =
                    userViewModel.currentUserEditable.value?.telegramPupilNumberOrId

                if (userTelegramID.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        R.string.user_full_information_fragment_add_name_toast,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    with(telegramIntent) {
                        data = Uri.parse("${Constants.TELEGRAM_HTTP}$userTelegramID")
                        setPackage(Constants.TELEGRAM_PACKAGE)
                    }
                    startActivity(telegramIntent)
                }
            }
            btnDeleteUser.setOnClickListener {
                AlertDialogProvider.createAlertInstance(
                    requireContext(),
                    R.layout.delete_user_alert_dialog,
                    onDelete = {
                        showDeleteToast()
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

    private fun showDeleteToast() {
        Toast.makeText(
            requireContext(),
            R.string.common_snack_bar_user_delete,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun deleteUser() {
        val user = userViewModel.currentUserEditable.value
        //TODO: Логически неправильно, что ты сначала навигируешься, а потом удаляешь
        // юзера. Удаление, к тому же, может быть неудачным. Тебе нужно показыавть состояние
        // загрузки пока идёт удаление (это пара миллисекунд, но тем не менее это логически верно)
        // и только после того, как удаление прошло удачно, выполнять остальную логику
        user?.let { userViewModel.deleteUser(it) }
        findNavController().navigate(R.id.action_userFullInformation_to_listFragmentBlack)
    }


    private fun initObservers() {
        userViewModel.currentUserEditable.observe(viewLifecycleOwner) { user ->
            with(binding) {
                Log.d("USERFULL", "user = $user")
                tvName.text = user.name
                tvGrade.text = "${user.grade} ${getString(R.string.common_grade_lowercase)}"
                Log.d("LOGSLOGS", "${tvGrade.text}")
                btnMakeCall.text = user.phonePupilNumber
            }
        }
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