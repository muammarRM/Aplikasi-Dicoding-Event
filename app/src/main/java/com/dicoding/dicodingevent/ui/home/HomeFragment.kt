package com.dicoding.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var upcomingEventAdapter: EventAdapter
    private lateinit var completedEventAdapter: EventAdapter
    private lateinit var searchResultsAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        setupSearch()

        viewModel.getUpcomingEvent()
        viewModel.getCompletedEvents()
    }

    private fun setupRecyclerView() {
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCompleted.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun observeData() {
        viewModel.upcomingEvent.observe(viewLifecycleOwner) { eventList ->
            eventList?.let {
                upcomingEventAdapter = EventAdapter(it) { event ->
                    val detailEvent = HomeFragmentDirections.actionNavigationHomeToDetailFragment(event.id)
                    findNavController().navigate(detailEvent)
                }
                binding.rvUpcoming.adapter = upcomingEventAdapter
            }
        }

        viewModel.completedEvents.observe(viewLifecycleOwner) { eventList ->
            eventList?.let {
                completedEventAdapter = EventAdapter(it) { event ->
                    val detailEvent = HomeFragmentDirections.actionNavigationHomeToDetailFragment(event.id)
                    findNavController().navigate(detailEvent)
                }
                binding.rvCompleted.adapter = completedEventAdapter
            }
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { eventList ->
            eventList?.let {
                searchResultsAdapter = EventAdapter(it) { event ->
                    val detailEvent = HomeFragmentDirections.actionNavigationHomeToDetailFragment(event.id)
                    findNavController().navigate(detailEvent)
                }
                binding.rvSearchResults.adapter = searchResultsAdapter
                binding.rvSearchResults.visibility = View.VISIBLE
                binding.rvUpcoming.visibility = View.GONE
                binding.rvCompleted.visibility = View.GONE
                binding.upcomingTitle.visibility = View.GONE
                binding.completedTitle.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchEvents(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    binding.rvSearchResults.visibility = View.GONE
                    binding.rvUpcoming.visibility = View.VISIBLE
                    binding.rvCompleted.visibility = View.VISIBLE
                    binding.upcomingTitle.visibility = View.VISIBLE
                    binding.completedTitle.visibility = View.VISIBLE
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
