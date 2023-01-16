package space.mel.tutorschedule.fragments.update

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.UpdateFragmentBinding
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.viewmodel.UserViewModel

class UpdateFragment : Fragment() {
    private lateinit var updateBinding: UpdateFragmentBinding
    private val args by navArgs<UpdateFragmentArgs>()

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        updateBinding = UpdateFragmentBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        updateBinding.edtUpdateFirstName.setText(args.currentUser.firstName)
        updateBinding.edtUpdateGrade.setText(args.currentUser.grade.toString())

        updateBinding.updateBtn.setOnClickListener {
            updateItem()
        }
        //add Delete menu
        setHasOptionsMenu(true)

        return updateBinding.root
    }

    private fun updateItem(){
        val firstName = updateBinding.edtUpdateFirstName.text.toString()
        val grade = Integer.parseInt(updateBinding.edtUpdateGrade.text.toString())

        if(inputCheck(firstName, updateBinding.edtUpdateGrade.text)){
            // Create User Object
            val updatedUser = User(args.currentUser.id, firstName, grade)
            // Update Current User
            userViewModel.updateUser(updatedUser)
            Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
            // Navigate Back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(firstName: String, age: Editable): Boolean{
        return !(TextUtils.isEmpty(firstName) && age.isEmpty())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
        userViewModel.deleteUser(args.currentUser)
            Toast.makeText(
                requireContext(),
                "Successfully removed ${args.currentUser.firstName}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_->

        }
        builder.setTitle("Delete ${args.currentUser.firstName}?")
        builder.setMessage("Are you sure want delete ${args.currentUser.firstName}?")
        builder.create().show()
    }
}