package edu.bluejack23_2.verky.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.verky.R

class InterestAdapter(private var items: List<String>) : RecyclerView.Adapter<InterestAdapter.RadioButtonViewHolder>() {

    private var selectedItemPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioButtonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_interest, parent, false)
        return RadioButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: RadioButtonViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class RadioButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkbox: CheckBox = itemView.findViewById(R.id.interestCheckbox)

        fun bind(item: String, position: Int) {
            checkbox.text = item

            itemView.setOnClickListener {
                selectedItemPosition = position
                notifyDataSetChanged()
            }
        }
    }

    fun setItems(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }
}