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
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class CurrentWeekListAdapter(context: Context, private val shifts: List<Date>) :
    ArrayAdapter<Date>(context, 0, shifts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_current_shifts, parent, false)

        val currentItem = getItem(position)

        // Find the views in the custom layout
        val imageView = listItemView.findViewById<ImageView>(R.id.list_item_img)
        val dateTextView = listItemView.findViewById<TextView>(R.id.list_item_date)
        var shiftText = ""



        return listItemView
    }
}
