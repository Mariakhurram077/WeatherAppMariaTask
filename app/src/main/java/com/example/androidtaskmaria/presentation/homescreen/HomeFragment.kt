package com.example.androidtaskmaria.presentation.homescreen

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtaskmaria.R
import com.example.androidtaskmaria.databinding.FragmentHomeBinding
import com.example.androidtaskmaria.presentation.homescreen.adapter.CitiesAdapter
import com.example.androidtaskmaria.presentation.visibility
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var citiesAdapter: CitiesAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        citiesAdapter =
            CitiesAdapter(onItemClick = {
                findNavController().navigate(R.id.detailScreenFragment)
            })

        binding.apply {
            rvCities.apply {
                adapter = citiesAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            searchIconButton.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weatherData.collect {
                citiesAdapter.submitList(it?.list)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weatherDataDaily.collect {
                Log.i("listOfData", "onViewCreated: ${it}")
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.homeScreenEvents.collect { event ->
                when (event) {
                    is HomeViewModel.HomeScreenEvents.ShowErrorMessage -> {
                        showAlertDialog(requireContext(), message = event.message)
                    }
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.loadingProgressBar.visibility(it)
        }
    }

    private fun showAlertDialog(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.alert))
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}

