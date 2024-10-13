package com.dicoding.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingevent.ui.EventAdapter

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UpcomingViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        // Setup RecyclerView
        binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcoming.setHasFixedSize(true)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(UpcomingViewModel::class.java)

        // Observe LiveData from ViewModel
        viewModel.upcomingEvent.observe(viewLifecycleOwner, Observer { eventList ->
            eventList?.let {
                eventAdapter = EventAdapter(it) // Mengisi adapter dengan event yang sudah selesai
                binding.rvUpcoming.adapter = eventAdapter
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
        viewModel.getUpcomingEvent()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
