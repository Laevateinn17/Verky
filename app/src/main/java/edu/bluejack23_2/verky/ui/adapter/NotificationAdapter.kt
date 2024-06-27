package edu.bluejack23_2.verky.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.Notification
import edu.bluejack23_2.verky.ui.view.dashboard.PublicProfileActivity

class NotificationAdapter(private var notificationList: List<Notification>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val notificationImage : ImageView = itemView.findViewById(R.id.item_notification_image)
        private val titleTextView: TextView = itemView.findViewById(R.id.item_notification_name)
        private val timeTextView: TextView = itemView.findViewById(R.id.item_notification_time)

        fun bind(notification: Notification) {
            notification.fromUser?.profile_picture?.let { url ->
                Glide.with(itemView)
                    .load(url)
                    .placeholder(R.color.gray_200)
                    .into(notificationImage)
            }
            titleTextView.text = notification.fromUser!!.name + " want's to connect with you"
            notification.timeStamp?.let {
                timeTextView.text = getTimeAgo(it)
            }

            notificationImage.setOnClickListener{
                val context = itemView.context
                val intent = Intent(context, PublicProfileActivity::class.java).apply {
                    putExtra("USER_DATA", notification.fromUser)
                }
                context.startActivity(intent)
            }
        }
    }

    private fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "$days days ago"
            hours > 0 -> "$hours hours ago"
            minutes > 0 -> "$minutes minutes ago"
            else -> "just now"
        }
    }

    fun updateData(newNotificationList: List<Notification>) {
        notificationList = newNotificationList
        notifyDataSetChanged()
    }
}
