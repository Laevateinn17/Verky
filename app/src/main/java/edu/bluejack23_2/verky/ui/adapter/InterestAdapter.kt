package edu.bluejack23_2.verky.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.verky.R

class InterestAdapter(private var items: List<String>, private var selectedInterest : List<String>, private val onItemCheckedChange: (List<String>) -> Unit) : RecyclerView.Adapter<InterestAdapter.CheckBoxViewHolder>() {

    private var selectedItems = mutableListOf<String>()

    init {
        selectedItems.addAll(selectedInterest)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckBoxViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_interest, parent, false)
        return CheckBoxViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckBoxViewHolder, position: Int) {
        holder.bind(items[position], selectedItems.contains(items[position]))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CheckBoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkbox: CheckBox = itemView.findViewById(R.id.interestCheckbox)

        fun bind(item: String, isChecked: Boolean) {
            checkbox.text = item
            checkbox.isChecked = isChecked

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!selectedItems.contains(item)) {
                        selectedItems.add(item)
                    }
                } else {
                    selectedItems.remove(item)
                }
                onItemCheckedChange(selectedItems)
            }
        }
    }

    fun setItems(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }
}