package edu.bluejack23_2.verky.ui.view.dashboard.chatfragment

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

        chatAdapter = ChatAdapter()

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

//        chatViewModel.

        chatViewModel.chatList.observe(viewLifecycleOwner, Observer { chats ->
            Log.d("ChatFragment", "Chats: $chats")
            chatAdapter.submitList(chats)
            binding.chatRecyclerView.scrollToPosition(chats.size - 1)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}