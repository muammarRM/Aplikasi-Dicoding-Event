package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.response.Event
import com.dicoding.dicodingevent.databinding.FragmentDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = activity?.findViewById<BottomNavigationView>(com.dicoding.dicodingevent.R.id.nav_view)
        bottomNav?.visibility = View.GONE

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        // Observe LiveData from ViewModel
        viewModel.eventDetail.observe(viewLifecycleOwner, Observer { event ->
            event?.let {
                bindDataToView(it)
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        // Get event detail using eventId passed from previous fragment
        viewModel.getEventDetail(args.eventId)
    }

    private fun bindDataToView(event: Event) {
        // Set event name with a descriptive message
        binding.tvEventName.text = event.name
        binding.tvEventDescription.text = event.summary

        // Owner name with context
        binding.tvOwnerName.text = "Organized by: ${event.ownerName}"

        // Display city name clearly
        binding.tvCityName.text = "Location: ${event.cityName}"

        // Begin and end time
        binding.tvBeginTime.text = "Start Time: ${event.beginTime}"
        binding.tvEndTime.text = "End Time: ${event.endTime}"

        // Display available slots
        binding.tvQuota.text = "${event.quota - event.registrants} slots available"

        // Load image using Glide
        Glide.with(requireContext())
            .load(event.mediaCover)
            .into(binding.ivMediaCover)

        val buttonLink: Button = binding.buttonLink

        buttonLink.setOnClickListener {
            val link = event.link // Pastikan Anda memiliki properti link di model Event
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bottomNav = activity?.findViewById<BottomNavigationView>(com.dicoding.dicodingevent.R.id.nav_view)
        bottomNav?.visibility = View.VISIBLE
    }
}
