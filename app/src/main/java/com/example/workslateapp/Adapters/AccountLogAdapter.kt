import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workslateapp.DataClasses.Shift
import com.example.workslateapp.R
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AccountLogAdapter(private val shifts: List<Shift>) : RecyclerView.Adapter<AccountLogAdapter.ShiftViewHolder>() {

    // ViewHolder for the shift item
    class ShiftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adapterLogInDate: TextView = itemView.findViewById(R.id.adapter_log_in_date)
        val adapterLogInTime: TextView = itemView.findViewById(R.id.adapter_log_in_time)
        val adapterLogOutDate: TextView = itemView.findViewById(R.id.adapter_log_out_date)
        val adapterLogOutTime: TextView = itemView.findViewById(R.id.adapter_log_out_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShiftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_account_log, parent, false)
        return ShiftViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShiftViewHolder, position: Int) {
        val shift = shifts[position]

        // Define formatters
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        // Bind log-in data
        val logInDate = shift.timeStamp.first?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        val logInTime = shift.timeStamp.first?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalTime()
        holder.adapterLogInDate.text = logInDate?.format(dateFormatter)
        holder.adapterLogInTime.text = logInTime?.format(timeFormatter)

        // Bind log-out data
        val logOutDate = shift.timeStamp.second?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        val logOutTime = shift.timeStamp.second?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalTime()
        holder.adapterLogOutDate.text = logOutDate?.format(dateFormatter)
        holder.adapterLogOutTime.text = logOutTime?.format(timeFormatter)
    }

    override fun getItemCount(): Int {
        return shifts.size
    }
}
