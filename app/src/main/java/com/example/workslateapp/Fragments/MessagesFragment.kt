package com.example.workslateapp.Fragments

import ChatAdapter
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workslateapp.DataClasses.Message
import com.example.workslateapp.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
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
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.FragmentMessagesChatRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            true
        )

        chatAdapter = ChatAdapter(messages)
        binding.FragmentMessagesChatRecyclerView.adapter = chatAdapter

        // Fetch initial messages
        DatabaseManeger.fetchMessages { fetchedMessages ->
            messages.clear()
            messages.addAll(fetchedMessages)
            chatAdapter.notifyDataSetChanged()
        }

        // Listen for real-time updates
        DatabaseManeger.listenForNewMessages { newMessage ->
            messages.add(0,newMessage)
            chatAdapter.notifyItemInserted(0)
            binding.FragmentMessagesChatRecyclerView.scrollToPosition(0)
        }

        // Set up click listeners
        initView()
    }

    private fun initView() {
        binding.FragmentMessagesFabSend.setOnClickListener {
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