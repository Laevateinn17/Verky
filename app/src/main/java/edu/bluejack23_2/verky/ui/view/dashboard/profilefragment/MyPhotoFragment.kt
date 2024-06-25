package edu.bluejack23_2.verky.ui.view.dashboard.profilefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.databinding.FragmentMyPhotoBinding
import edu.bluejack23_2.verky.ui.adapter.MyPhotoAdapter
import edu.bluejack23_2.verky.ui.viewmodel.MyPhotoViewModel

class MyPhotoFragment : Fragment() {

    private var _binding: FragmentMyPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var photoAdapter: MyPhotoAdapter
    private val photoViewModel: MyPhotoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPhotoBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.photoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        photoViewModel.photos.observe(viewLifecycleOwner) { photoList ->
            photoAdapter = MyPhotoAdapter(photoList)
            binding.photoRecyclerView.adapter = photoAdapter
        }

        val loggedUser = LoggedUser.getInstance().getUser()
        loggedUser?.let {
            photoViewModel.setPhotos(it.gallery_picture)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}