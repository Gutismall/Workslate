import android.content.Context
import androidx.core.content.ContextCompat
import com.example.workslateapp.R
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.time.ZoneId

class CalendarDecorator(
    private val dates: List<LocalDate>, // List of dates to highlight
    private val context: Context
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        // Return true for dates that should be highlighted
        return dates.contains(day.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
    }

    override fun decorate(view: DayViewFacade) {
        // Customize the background of the date
        val drawable = ContextCompat.getDrawable(context, R.drawable.dec_calendar_highlight)
        view.setBackgroundDrawable(drawable!!)
    }
}