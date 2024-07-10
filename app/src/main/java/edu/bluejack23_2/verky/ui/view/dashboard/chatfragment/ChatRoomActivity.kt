package edu.bluejack23_2.verky.ui.view.dashboard.chatfragment

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.chat.ChatRepositoryImpl
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.Message
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.databinding.ActivityChatRoomBinding
import edu.bluejack23_2.verky.ui.adapter.MessageAdapter
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel
import edu.bluejack23_2.verky.ui.viewmodel.ChatViewModel
import edu.bluejack23_2.verky.ui.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatRoomBinding
    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this).get(ChatViewModel::class.java)
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private val messageAdapter = MessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatId = intent.getStringExtra("CHAT_ID")
        val userId = intent.getStringExtra("USER_ID")

        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatRoomActivity)
            adapter = messageAdapter
        }

        binding.backButton.setOnClickListener {
            onBackPressed();
        }

        chatId?.let {
            viewModel.fetchMessage(it).observe(this, Observer { messages ->
                val sortedMessages = messages.sortedBy { it.timestamp }
                messageAdapter.submitList(sortedMessages)
                binding.chatRecyclerView.post {
                    binding.chatRecyclerView.smoothScrollToPosition(sortedMessages.size - 1)
                }
            })
        }

        userId?.let {
            fetchUser(it)
        }

        binding.sendButton.setOnClickListener {
            val messageContent = binding.chatContent.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                sendMessage(messageContent)
                binding.chatContent.text.clear()
            }
        }
    }

    private fun sendMessage(message : String){
        val chatId = intent.getStringExtra("CHAT_ID")

        chatId?.let { chatId ->
                val message = LoggedUser.getInstance().getUser()?.id?.let {
                    Message(
                        messageId = "",
                        content = message,
                        status = false,
                        timestamp = System.currentTimeMillis(),
                        userID = it,
                        user = null
                    )
                }
            if (message != null) {
                viewModel.sendMessage(chatId, message)
            }
        }
    }

    private fun fetchUser(userId : String){
        GlobalScope.launch (Dispatchers.Main) {
            try {
                val user = userViewModel.fetchUser(userId)
                binding.name.text = user.name
                Glide.with(this@ChatRoomActivity)
                    .load(user.profile_picture)
                    .placeholder(R.color.gray_200)
                    .into(binding.chatProfile)
            } catch (e: Exception) {
                Log.e("ChatRoomActivity", "Error fetching user: ${e.message}")
            }
        }
    }
}