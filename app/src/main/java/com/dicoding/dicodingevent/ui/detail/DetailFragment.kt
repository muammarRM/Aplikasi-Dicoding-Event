package com.dicoding.dicodingevent.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.response.Event
import com.dicoding.dicodingevent.databinding.FragmentDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
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

        hideBottomNavigationView()
        observeViewModel()
        setHasOptionsMenu(true)
        viewModel.getEventDetail(args.eventId)
    }

    private fun hideBottomNavigationView() {
        activity?.findViewById<BottomNavigationView>(com.dicoding.dicodingevent.R.id.nav_view)?.visibility = View.GONE
    }

    private fun observeViewModel() {
        viewModel.eventDetail.observe(viewLifecycleOwner) { event ->
            event?.let {
                bindDataToView(it)
                (requireActivity() as AppCompatActivity).supportActionBar?.title = it.name
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

    @SuppressLint("SetTextI18n")
    private fun bindDataToView(event: Event) {
        binding.apply {
            tvEventName.text = event.name
            tvEventDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvOwnerName.text = "Organized by: ${event.ownerName}"
            tvCityName.text = "Location: ${event.cityName}"
            tvBeginTime.text = "Start Time: ${event.beginTime}"
            tvEndTime.text = "End Time: ${event.endTime}"
            tvQuota.text = "${event.quota - event.registrants} slots available"

            Glide.with(requireContext())
                .load(event.mediaCover)
                .into(ivMediaCover)

            buttonLink.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(event.link)))
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<BottomNavigationView>(com.dicoding.dicodingevent.R.id.nav_view)?.visibility = View.VISIBLE
        _binding = null
    }
}
