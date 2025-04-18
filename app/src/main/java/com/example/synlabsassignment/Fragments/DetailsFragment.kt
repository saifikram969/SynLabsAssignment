package com.example.synlabsassignment.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.synlabsassignment.ViewModel.PlacesViewModel
import com.example.synlabsassignment.ViewModel.PlacesViewModelFactory
import com.example.synlabsassignment.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PlacesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel with factory
        val factory = PlacesViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory).get(PlacesViewModel::class.java)

        arguments?.getString("placeId")?.let { placeId ->
            viewModel.getPlaceDetails(placeId)
            observePlaceDetails()
        }
    }

    private fun observePlaceDetails() {
        viewModel.selectedPlace.observe(viewLifecycleOwner) { place ->
            place?.let {
                // Update UI with place details
                binding.placeName.text = it.name
                binding.placeDescription.text = it.description
                // Add more UI updates as needed
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                // Show error message
                viewModel.clearError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}