package com.example.dicoapp.ui.finished_event

import android.os.Bundle
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
import com.example.dicoapp.databinding.FragmentFinishedBinding
import com.example.dicoapp.ui.ViewModelFactory
import com.example.dicoapp.utils.EventType

class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinishedViewModel by viewModels() {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventSearchAdapter: EventAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        observeViewModel()

    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(EventType.FINISHED) { event ->
            val bundle = Bundle().apply {
                putString(EXTRA_EVENT_ID, event.id.toString())
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }

        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FinishedFragment.eventAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setupWithSearchBar(binding.searchBarEvent)

        eventSearchAdapter = EventAdapter(EventType.FINISHED) { event ->
            val bundle = Bundle().apply {
                putString(EXTRA_EVENT_ID, event.id.toString())
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }
        eventSearchAdapter = EventAdapter(EventType.FINISHED) { event ->
            val bundle = Bundle().apply {
                putString(EXTRA_EVENT_ID, event.id.toString())
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }

        binding.rvSearchUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FinishedFragment.eventSearchAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.listEvents.observe(viewLifecycleOwner) { events ->
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
            binding.apply {
                if (events.isEmpty() && searchView.text.isNotBlank()) {
                    tvSearchMessage.visibility = View.VISIBLE
                    rvSearchUpcomingEvents.visibility = View.INVISIBLE
                    tvSearchMessage.text = getString(R.string.event_not_found)
                } else if (events.isNotEmpty()) {
                    tvSearchMessage.visibility = View.INVISIBLE
                    rvSearchUpcomingEvents.visibility = View.VISIBLE
                    eventSearchAdapter.submitList(events)
                }
            }
        }
    }


    companion object {
        //        private const val TAG = "FinishedFragment"
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}