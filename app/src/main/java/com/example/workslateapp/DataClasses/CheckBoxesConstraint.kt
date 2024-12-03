import android.widget.CheckBox
import com.example.workslateapp.databinding.FragmentConstraintsBinding

data class CheckBoxesConstraint(
    val sundayMorning: CheckBox,
    val sundayEvening: CheckBox,
    val sundayNight: CheckBox,
    val mondayMorning: CheckBox,
    val mondayEvening: CheckBox,
    val mondayNight: CheckBox,
    val tuesdayMorning: CheckBox,
    val tuesdayEvening: CheckBox,
    val tuesdayNight: CheckBox,
    val wednesdayMorning: CheckBox,
    val wednesdayEvening: CheckBox,
    val wednesdayNight: CheckBox,
    val thursdayMorning: CheckBox,
    val thursdayEvening: CheckBox,
    val thursdayNight: CheckBox,
    val fridayMorning: CheckBox,
    val fridayEvening: CheckBox,
    val fridayNight: CheckBox,
    val saturdayMorning: CheckBox,
    val saturdayEvening: CheckBox,
    val saturdayNight: CheckBox
) {
    val allCheckBoxes: List<CheckBox>
        get() = listOf(
            sundayMorning, sundayEvening, sundayNight,
            mondayMorning, mondayEvening, mondayNight,
            tuesdayMorning, tuesdayEvening, tuesdayNight,
            wednesdayMorning, wednesdayEvening, wednesdayNight,
            thursdayMorning, thursdayEvening, thursdayNight,
            fridayMorning, fridayEvening, fridayNight,
            saturdayMorning, saturdayEvening, saturdayNight
        )
}

fun createConstraintCheckBoxes(binding: FragmentConstraintsBinding): CheckBoxesConstraint {
    return CheckBoxesConstraint(
        sundayMorning = binding.FragmentConstraintsCheckBoxSundayMorning,
        sundayEvening = binding.FragmentConstartinsCheckBoxSundayEvening,
        sundayNight = binding.FragmentConstraintsCheckBoxSundayNight,
        mondayMorning = binding.FragmentConstraintsCheckBoxMondayMorning,
        mondayEvening = binding.FragmentConstartinsCheckBoxMondayEvening,
        mondayNight = binding.FragmentConstraintsCheckBoxMondayNight,
        tuesdayMorning = binding.FragmentConstraintsCheckBoxTuesdayMorning,
        tuesdayEvening = binding.FragmentConstraintsCheckBoxTuesdayEvening,
        tuesdayNight = binding.FragmentConstraintsCheckBoxTuesdayNight,
        wednesdayMorning = binding.FragmentConstraintsCheckBoxWednesdayMorning,
        wednesdayEvening = binding.FragmentConstraintsCheckBoxWednesdayEvening,
        wednesdayNight = binding.FragmentConstraintsCheckBoxWednesdayNight,
        thursdayMorning = binding.FragmentConstraintsCheckBoxThursdayMorning,
        thursdayEvening = binding.FragmentConstraintsCheckBoxThursdayEvening,
        thursdayNight = binding.FragmentConstraintsCheckBoxThursdayNight,
        fridayMorning = binding.FragmentConstraintsCheckBoxFridayMorning,
        fridayEvening = binding.FragmentConstraintsCheckBoxFridayEvening,
        fridayNight = binding.FragmentConstraintsCheckBoxFridayNight,
        saturdayMorning = binding.FragmentConstraintsCheckBoxSaturdayMorning,
        saturdayEvening = binding.FragmentConstraintsCheckBoxSaturdayEvening,
        saturdayNight = binding.FragmentConstraintsCheckBoxSaturdayNight
    )
}