package edu.bluejack23_2.verky.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel

class GalleryAdapter(
    private val fragment: Fragment,
    private val viewModel: AuthViewModel
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.clearSearchButton)

        init {
            imageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                fragment.startActivityForResult(intent, PICK_IMAGE_REQUEST + adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewModel.images.observe(fragment.viewLifecycleOwner, Observer { images ->
            holder.imageView.setImageURI(images[position])
        })
    }

    override fun getItemCount(): Int = viewModel.images.value?.size ?: 0
}