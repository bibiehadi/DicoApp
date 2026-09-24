package com.example.dicoapp.ui.upcoming_event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicoapp.R
import com.example.dicoapp.adapter.EventAdapter
import com.example.dicoapp.adapter.EventHorizontalCardAdapter
import com.example.dicoapp.databinding.FragmentUpcomingBinding
import com.example.dicoapp.ui.finished_event.FinishedFragment

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpcomingViewModel by viewModels()
    private lateinit var eventAdapter: EventHorizontalCardAdapter
    private lateinit var eventSearchAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupSearchView() {
        binding.searchView.setupWithSearchBar(binding.searchBarEvent)

        eventSearchAdapter = EventAdapter { event ->
            Log.d(TAG, "navigate to detail page")
            val bundle = Bundle().apply {
                putString(EXTRA_EVENT_ID, event.id.toString())
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }

        eventSearchAdapter = EventAdapter { event ->
            val bundle = Bundle().apply {
                putString(FinishedFragment.EXTRA_EVENT_ID, event.id.toString())
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }

        binding.rvSearchUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UpcomingFragment.eventSearchAdapter
        }
    }


    private fun setupRecyclerView() {
        binding.tvMessage.visibility = View.INVISIBLE
        eventAdapter = EventHorizontalCardAdapter {
        }

        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UpcomingFragment.eventAdapter
        }

    }

    private fun observeViewModel() {
        viewModel.listEvents.observe(viewLifecycleOwner) { events ->
            Log.d(TAG, events.size.toString())
            if (events.isEmpty()) {
                binding.tvMessage.visibility = View.VISIBLE
                binding.tvMessage.text =
                    getString(R.string.upcoming_event_not_found)
            }
            eventAdapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.INVISIBLE
        }

        viewModel.snackBarText.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }


        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchView.text.toString()
            binding.tvSearchMessage.visibility = View.INVISIBLE
            eventSearchAdapter.submitList(null)
            viewModel.onSearchQuery(query)
            false
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading)
                binding.progressBarSearch.visibility = View.VISIBLE
            else
                binding.progressBarSearch.visibility = View.INVISIBLE
        }

        viewModel.searchedEvents.observe(viewLifecycleOwner) { events ->
            Log.d(TAG, events.size.toString())
            if (events.isEmpty() && binding.searchView.text.isNotBlank()) {
                binding.tvSearchMessage.visibility = View.VISIBLE
                binding.rvSearchUpcomingEvents.visibility = View.INVISIBLE
                binding.tvSearchMessage.text = getString(R.string.event_not_found)
            } else if (events.isNotEmpty()) {
                binding.tvSearchMessage.visibility = View.INVISIBLE
                binding.rvSearchUpcomingEvents.visibility = View.VISIBLE
                eventSearchAdapter.submitList(events)
            }
        }

    }


    companion object {
        private const val TAG = "UpcomingFragment"
        const val EXTRA_EVENT_ID = "extra_event_id"

    }
}