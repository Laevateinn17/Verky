package edu.bluejack23_2.verky.ui.view.dashboard.chatfragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.data.model.Chat
import edu.bluejack23_2.verky.databinding.FragmentChatBinding
import edu.bluejack23_2.verky.ui.adapter.ChatAdapter
import edu.bluejack23_2.verky.ui.viewmodel.ChatViewModel

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter
    private val chatViewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatAdapter{
            chat -> navigateToChatDetail(chat)
        }

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        binding.notificationButton.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }

        chatViewModel.chatList.observe(viewLifecycleOwner, Observer { chats ->
            if (chats.isEmpty()) {
                binding.noDataTextView.visibility = View.VISIBLE
                binding.chatRecyclerView.visibility = View.GONE
            } else {
                binding.noDataTextView.visibility = View.GONE
                binding.chatRecyclerView.visibility = View.VISIBLE
                chatAdapter.submitList(chats)
                binding.chatRecyclerView.scrollToPosition(chats.size - 1)
            }
        })

    }

    private fun navigateToChatDetail(chat: Chat) {
        Log.d("here", "clicked")
        val intent = Intent(requireContext(), ChatRoomActivity::class.java).apply {
            putExtra("CHAT_ID", chat.chatId)
            putExtra("USER_ID", chat.partnerUser?.id)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}