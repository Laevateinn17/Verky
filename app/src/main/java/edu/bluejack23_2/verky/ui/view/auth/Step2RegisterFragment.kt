package edu.bluejack23_2.verky.ui.view.auth

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.databinding.FragmentStep2RegisterBinding
import edu.bluejack23_2.verky.ui.adapter.InterestAdapter
import edu.bluejack23_2.verky.ui.adapter.ReligionAdapter
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel


@AndroidEntryPoint
class Step2RegisterFragment : Fragment() {


    private var _binding: FragmentStep2RegisterBinding? = null
    private val binding get() = _binding!!

    private var listener: OnContinueListener? = null
    private lateinit var religionRecyclerView: RecyclerView
    private lateinit var religionAdapter : ReligionAdapter
    private lateinit var interestAdapter: InterestAdapter
    private lateinit var interestRecyclerView: RecyclerView
    private lateinit var authViewModel : AuthViewModel

    interface OnContinueListener {
        fun goToFragmentRegist3()
        fun goToFragmentRegist1()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnContinueListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnContinueListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep2RegisterBinding.inflate(inflater, container, false)
        religionRecyclerView = binding.religionRecyclerView
        interestRecyclerView = binding.interestRecyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.continueButton.setOnClickListener {
            listener?.goToFragmentRegist3()
        }

        binding.backButton.setOnClickListener{
            listener?.goToFragmentRegist1()
        }

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        authViewModel.fetchReligionData()
        authViewModel.fetchInterestData()

        val religionLayoutManager = FlexboxLayoutManager(context)
        religionLayoutManager.flexDirection = FlexDirection.ROW
        religionLayoutManager.justifyContent = JustifyContent.FLEX_START

        val interestLayoutManager = FlexboxLayoutManager(context)
        interestLayoutManager.flexDirection = FlexDirection.ROW
        interestLayoutManager.justifyContent = JustifyContent.FLEX_START

        religionAdapter = ReligionAdapter(emptyList())
        religionRecyclerView.layoutManager = religionLayoutManager
        religionRecyclerView.adapter = religionAdapter

        interestAdapter = InterestAdapter(emptyList())
        interestRecyclerView.layoutManager = interestLayoutManager
        interestRecyclerView.adapter = interestAdapter

        observeViewModel()
    }

    private fun observeViewModel() {
        authViewModel.religionData.observe(viewLifecycleOwner) { religions ->
            religionAdapter.setItems(religions)
        }
        authViewModel.interestData.observe(viewLifecycleOwner) { interests ->
            interestAdapter.setItems(interests)
        }
    }

}