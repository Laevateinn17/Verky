package edu.bluejack23_2.verky.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.verky.R

class PublicInterestAdapter(private val interests: List<String>) :
    RecyclerView.Adapter<PublicInterestAdapter.InterestViewHolder>() {

    inner class InterestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val interestCheckBox: CheckBox = itemView.findViewById(R.id.interestCheckbox)

        fun bind(interest: String) {
            interestCheckBox.text = interest
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_interest_view, parent, false)
        return InterestViewHolder(view)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val interest = interests[position]
        holder.bind(interest)
    }

    override fun getItemCount(): Int {
        return interests.size
    }
}