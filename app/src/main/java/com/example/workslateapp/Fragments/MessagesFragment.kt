package com.example.workslateapp.Fragments
import ChatAdapter
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.workslateapp.DataClasses.Message
import com.example.workslateapp.databinding.FragmentMassagesBinding

class MessagesFragment : Fragment() {

    private var _binding: FragmentMassagesBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter
    private var messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // No need to fetch messages here; handle this in onViewCreated
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMassagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatAdapter(messages)
        binding.FragmentMassagesChatRecyclerView.adapter = chatAdapter

        // Fetch initial messages
        DatabaseManeger.fetchMessages { fetchedMessages ->
            messages.clear()
            messages.addAll(fetchedMessages)
            chatAdapter.notifyDataSetChanged()
        }

        // Listen for real-time updates
        DatabaseManeger.listenForNewMessages { newMessage ->
            messages.add(newMessage)
            chatAdapter.notifyItemInserted(messages.size - 1)
            binding.FragmentMassagesChatRecyclerView.scrollToPosition(messages.size - 1)
        }

        // Set up click listeners
        initView()
    }

    private fun initView() {
        binding.FragmentMessagesFABSend.setOnClickListener {
            val currentMessage = binding.FragmentMessagesTextBox.text.toString()
            if (currentMessage.isBlank()) {
                Toast.makeText(context, "Enter a message", Toast.LENGTH_SHORT).show()
            } else {
                DatabaseManeger.sendChatMessage(currentMessage)
                binding.FragmentMessagesTextBox.text.clear() // Clear input after sending
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}