package com.dicoding.dicodingevent.ui.completed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentCompletedBinding
import com.dicoding.dicodingevent.ui.EventAdapter
import com.dicoding.dicodingevent.ui.EventItem
import com.dicoding.dicodingevent.ui.EventViewModel
import com.dicoding.dicodingevent.viewmodel.ViewModelFactory

class CompletedFragment : Fragment() {
    private var _binding: FragmentCompletedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCompleted.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompleted.setHasFixedSize(true)

        eventAdapter = EventAdapter { eventItem ->
            when (eventItem) {
                is EventItem.Regular -> {
                    val detailEvent = CompletedFragmentDirections.actionNavigationCompletedToDetailFragment(eventItem.event.id)
                    findNavController().navigate(detailEvent)
                }
                is EventItem.Favorite -> {
                    val detailEvent = CompletedFragmentDirections.actionNavigationCompletedToDetailFragment(eventItem.event.id)
                    findNavController().navigate(detailEvent)
                }
            }
        }
        binding.rvCompleted.adapter = eventAdapter

        viewModel.getCompletedEvent()
        observeData()
    }

    private fun observeData() {
        viewModel.completedEvent.observe(viewLifecycleOwner) { eventList ->
            eventList?.let {
                eventAdapter.submitList(it.map { event -> EventItem.Regular(event) })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
