package com.example.synlabsassignment.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.synlabsassignment.R
import com.example.synlabsassignment.ViewModel.PlacesViewModel
import com.example.synlabsassignment.ViewModel.PlacesViewModelFactory
import com.example.synlabsassignment.adapters.PlacesAdapter
import com.example.synlabsassignment.databinding.FragmentResultsBinding
import com.google.android.material.snackbar.Snackbar

class ResultsFragment : Fragment() {
    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlacesViewModel
    private lateinit var adapter: PlacesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireActivity().application
        val viewModelFactory = PlacesViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlacesViewModel::class.java)

        val cityName = arguments?.getString("cityName") ?: ""
        if (cityName.isNotEmpty()) {
            viewModel.getPlacesForCity(cityName)
        }

        setupRecyclerView()
        setupObservers()
        setupFilterChips()
    }

    private fun setupRecyclerView() {
      /*  adapter = PlacesAdapter { place ->
            val bundle = Bundle().apply {
                putString("placeId", place.id)
            }
            findNavController().navigate(R.id.action_resultsFragment_to_detailsFragment, bundle)
        }*/
        adapter = PlacesAdapter { place ->
            val action = ResultsFragmentDirections.actionResultsFragmentToDetailsFragment(place.id)
            findNavController().navigate(action)
        }

        binding.placesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ResultsFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        viewModel.places.observe(viewLifecycleOwner) { places ->
            adapter.submitList(places)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun setupFilterChips() {
        binding.chipGroup.check(R.id.chip_all)

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_all -> viewModel.places.value?.let { adapter.submitList(it) }
                R.id.chip_attractions -> viewModel.filterPlacesByType("attraction")
                R.id.chip_restaurants -> viewModel.filterPlacesByType("restaurant")
                R.id.chip_museums -> viewModel.filterPlacesByType("museum")
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}