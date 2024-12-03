import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workslateapp.DataClasses.Message
import com.example.workslateapp.R

class ChatAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.Adapter_Chat_Message)
        val senderText: TextView = view.findViewById(R.id.Adapter_Chat_Sender)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_chat_adapter, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.message
        holder.senderText.text = message.sender
    }

    override fun getItemCount(): Int = messages.size
}