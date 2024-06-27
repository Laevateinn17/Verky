package edu.bluejack23_2.verky.ui.adapter

import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Typeface
import android.text.Spannable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.Notification

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
            val name = notification.fromUser!!.name
            val fullText = "$name wants to connect with you."

            val spannableString = SpannableString(fullText)

            val start = fullText.indexOf(name)
            val end = start + name.length

            spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            titleTextView.text = spannableString
        }
    }

    fun updateData(newNotificationList: List<Notification>) {
        notificationList = newNotificationList
        notifyDataSetChanged()
    }
}
