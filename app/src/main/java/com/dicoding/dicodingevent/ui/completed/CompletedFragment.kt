package com.dicoding.dicodingevent.ui.completed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentCompletedBinding
import com.dicoding.dicodingevent.ui.EventAdapter

class CompletedFragment : Fragment() {
    private var _binding: FragmentCompletedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CompletedViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedBinding.inflate(inflater, container, false)

        // Setup RecyclerView
        binding.rvCompleted.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompleted.setHasFixedSize(true)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(CompletedViewModel::class.java)

        // Observe LiveData from ViewModel
        viewModel.completedEvent.observe(viewLifecycleOwner, Observer { eventList ->
            eventList?.let {
                eventAdapter = EventAdapter(it) // Mengisi adapter dengan event yang sudah selesai
                binding.rvCompleted.adapter = eventAdapter
            }
        })

        // Handle loading state
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        // Handle error state
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch data
        viewModel.getCompletedEvent()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
