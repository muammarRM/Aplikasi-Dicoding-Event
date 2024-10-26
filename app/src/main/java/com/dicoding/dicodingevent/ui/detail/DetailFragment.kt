package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.databinding.FragmentDetailBinding
import com.dicoding.dicodingevent.ui.EventViewModel
import com.dicoding.dicodingevent.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        hideBottomNavigationView()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = args.eventId
        viewModel.getDetailEvent(eventId)


        eventId.let { id ->
            viewModel.getEventById(id).observe(viewLifecycleOwner) { favoritedEvent ->
                binding.fabFavorite.setImageResource(
                    if (favoritedEvent == null) R.drawable.baseline_favorite_border_24 else R.drawable.baseline_favorite_24
                )
                binding.fabFavorite.setOnClickListener {
                    val currentEvent = viewModel.detailEvent.value
                    currentEvent?.let { event ->
                        if (favoritedEvent == null) {
                            val favorite = EventEntity(
                                id = event.id,
                                name = event.name,
                                mediaCover = event.mediaCover,
                                summary = event.summary,
                                description = event.description,
                                ownerName = event.ownerName,
                                cityName = event.cityName,
                                quota = event.quota,
                                registrants = event.registrants,
                                beginTime = event.beginTime,
                                endTime = event.endTime,
                                category = event.category,
                                link = event.link,
                                imageLogo = event.imageLogo
                            )
                            viewModel.insertEvent(favorite)
                        } else {
                            viewModel.deleteEvent(favoritedEvent)
                        }
                    }
                }
            }
            viewModel.detailEvent.observe(viewLifecycleOwner) { event ->
                binding.apply {
                    tvEventName.text = event.name
                    tvEventDescription.text =
                        HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    tvOwnerName.text = "Organized by: ${event.ownerName}"
                    tvCityName.text = "Location: ${event.cityName}"
                    tvBeginTime.text = "Start Time: ${event.beginTime}"
                    tvEndTime.text = "End Time: ${event.endTime}"
                    tvQuota.text = "${event.quota - event.registrants} slots available"

                    Glide.with(requireContext())
                        .load(event.mediaCover)
                        .into(ivMediaCover)

                    buttonLink.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                        startActivity(intent)
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
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
        _binding = null
    }

    private fun hideBottomNavigationView() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }
}
