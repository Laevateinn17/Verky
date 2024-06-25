package edu.bluejack23_2.verky.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.verky.R

class ReligionAdapter(private var items: List<String>) : RecyclerView.Adapter<ReligionAdapter.RadioButtonViewHolder>() {

    private var selectedItemPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioButtonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_religion, parent, false)
        return RadioButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: RadioButtonViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class RadioButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val radioButton: RadioButton = itemView.findViewById(R.id.religionRadioButton)

        fun bind(item: String, position: Int) {
            radioButton.text = item
            radioButton.isChecked = position == selectedItemPosition

            itemView.setOnClickListener {
                selectedItemPosition = position
                notifyDataSetChanged()
            }

            radioButton.setOnClickListener {
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