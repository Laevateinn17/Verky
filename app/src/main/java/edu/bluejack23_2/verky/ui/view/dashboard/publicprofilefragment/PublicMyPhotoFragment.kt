package edu.bluejack23_2.verky.ui.view.dashboard.publicprofilefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.databinding.FragmentMyPhotoBinding
import edu.bluejack23_2.verky.databinding.FragmentPublicMyPhotoBinding
import edu.bluejack23_2.verky.ui.adapter.MyPhotoAdapter
import edu.bluejack23_2.verky.ui.viewmodel.MyPhotoViewModel


class PublicMyPhotoFragment : Fragment() {

    private var _binding: FragmentPublicMyPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var photoAdapter: MyPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<User>("USER_DATA")?.let { user ->
            setContent(user)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPublicMyPhotoBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.photoRecyclerView.layoutManager = GridLayoutManager(context, 3)

        return view
    }

    fun setContent(user: User?) {
        if (user != null) {
            photoAdapter = MyPhotoAdapter(user.gallery_picture)
            binding.photoRecyclerView.adapter = photoAdapter
        }
    }


}