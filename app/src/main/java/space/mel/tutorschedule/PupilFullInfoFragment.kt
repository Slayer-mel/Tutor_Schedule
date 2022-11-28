package space.mel.tutorschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import space.mel.tutorschedule.databinding.FragmentPupilFullInfoBinding
import space.mel.tutorschedule.model.Pupil

class PupilFullInfoFragment : Fragment() {
    lateinit var pupilFullInfoBinding: FragmentPupilFullInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pupilFullInfoBinding = FragmentPupilFullInfoBinding.inflate(layoutInflater)
        return pupilFullInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getSerializable("UserFullInformation")
        if (data != null) {
            setContent(data as Pupil)
        }
    }

    fun setContent(data: Pupil) {
        pupilFullInfoBinding.tvFullInfo.text = data.name
    }
}
