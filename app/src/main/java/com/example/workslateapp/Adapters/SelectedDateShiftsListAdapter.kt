import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.workslateapp.DataClasses.Shift
import com.example.workslateapp.DataClasses.ShiftType
import com.example.workslateapp.R

class SelectedDateShiftsListAdapter(context: Context, private val data: List<String>) :
    ArrayAdapter<String>(context, 0, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_current_shifts, parent, false)

        // Get the current shift and corresponding user at the same position
        // Find the views in the custom layout
        val imageView = listItemView.findViewById<ImageView>(R.id.Adapter_List_ShiftIcon)
        val dateTextView = listItemView.findViewById<TextView>(R.id.Adapter_List_ShiftTitle)
        var shiftText = ""

        when(position){
            0 -> {
                imageView.setImageResource(R.drawable.ic_morning)
                shiftText = "Morning Shift: "
            }
            1 -> {
                imageView.setImageResource(R.drawable.ic_evening)
                shiftText = "Afternoon Shift: "
            }
            2 ->{
                imageView.setImageResource(R.drawable.ic_night)
                shiftText = "Night Shift: "
            }
        }

        dateTextView.text = "$shiftText ${getItem(position)}"
        return listItemView
    }
}
