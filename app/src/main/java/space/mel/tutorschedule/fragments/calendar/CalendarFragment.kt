package space.mel.tutorschedule.fragments.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import space.mel.tutorschedule.databinding.CalendarFragmentBinding

class CalendarFragment: Fragment() {

private lateinit var calendarFragmentBinding: CalendarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        calendarFragmentBinding = CalendarFragmentBinding.inflate(layoutInflater)
        return calendarFragmentBinding.root
    }
}