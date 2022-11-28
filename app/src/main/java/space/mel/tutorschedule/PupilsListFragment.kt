package space.mel.tutorschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import space.mel.tutorschedule.databinding.FragmentPupilsListBinding
import space.mel.tutorschedule.model.Pupil
import space.mel.tutorschedule.model.PupilData
import space.mel.tutorschedule.view.UserAdapter


class PupilsListFragment : Fragment() {
    lateinit var pupilsListBinding: FragmentPupilsListBinding
    private lateinit var addBtn:FloatingActionButton
    private lateinit var recv:RecyclerView
    private lateinit var userList:ArrayList<PupilData>
    private lateinit var userAdapter: UserAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pupilsListBinding = FragmentPupilsListBinding.inflate(layoutInflater)
        return pupilsListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userList = ArrayList()
        addBtn = pupilsListBinding.addingBtn
        recv = pupilsListBinding.mRecycler
        userAdapter = UserAdapter(view.context, userList) //this
        /**setRecycler view Adapter*/
        recv.layoutManager = LinearLayoutManager(context) //this
        recv.adapter = userAdapter
        /**set Dialog*/
        addBtn.setOnClickListener { addInfo() }
    }
    private fun addInfo() {
        val inflter = LayoutInflater.from(context) //this
        val v = inflter.inflate(R.layout.add_item,null)
        /**set view*/
        val userName = v.findViewById<EditText>(R.id.userName)
        val userNo = v.findViewById<EditText>(R.id.userNo)

        val addDialog = AlertDialog.Builder(v.context) //this

        addDialog.setView(v)
        addDialog.setPositiveButton("Добавить"){
                dialog,_->
            val names = userName.text.toString()
            val number = userNo.text.toString().toInt()
            userList.add(PupilData(name = names, grade = "${number}"))
            userAdapter.notifyDataSetChanged()
            Toast.makeText(v.context,"Ученик успешно добавлен",Toast.LENGTH_SHORT).show() //this
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Отмена"){
                dialog,_->
            dialog.dismiss()
            Toast.makeText(v.context,"Отмена",Toast.LENGTH_SHORT).show()

        }
        addDialog.create()
        addDialog.show()
       /* initAdapter()
        setOnClickListeners()*/
    }

    /*private fun setOnClickListeners() {
        pupilsListBinding.btnAdd.setOnClickListener {
            val pupil = Pupil(
                name = pupilsListBinding.edtEnterName.text.toString(),
                grade = pupilsListBinding.edtEnterGrade.text.toString().toInt()
            )
            pupilsListAdapter?.addItem(pupil)

            pupilsListBinding.edtEnterName.text.clear()
            pupilsListBinding.edtEnterGrade.text.clear()
        }
    }

    private fun initAdapter() {
        pupilsListAdapter = PupilRecyclerViewAdapter(
            onClick = ::startPupilFullInformation
        )
        pupilsListBinding.rvPupilList.adapter = pupilsListAdapter
    }*/

    fun startPupilFullInformation(pupil: Pupil) {

        val bundle = Bundle()
        bundle.putSerializable(
            "PupilFullInformation",
            pupil
        )
        findNavController().navigate(
            R.id.action_pupilsListFragment_to_pupilFullInfoFragment,
            bundle
        )
    }

    /*fun checkFieldIsEmpty(): Boolean {
        val isNotOk = pupilsListBinding.edtEnterName.text.isEmpty() ||
                pupilsListBinding.edtEnterGrade.text.isEmpty()
        if (isNotOk) {
            Toast.makeText(this, "Введи имя", Toast.LENGTH_LONG).show()
        }
        return isNotOk
    }*/

}