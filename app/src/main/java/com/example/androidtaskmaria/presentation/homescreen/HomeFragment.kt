package com.example.androidtaskmaria.presentation.homescreen

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
import com.example.androidtaskmaria.databinding.FragmentHomeBinding
import com.example.androidtaskmaria.presentation.AlertDialog
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
            CitiesAdapter(onItemClick = { item ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailScreenFragment(
                        item
                    )
                )
            })

        binding.apply {
            rvCities.apply {
                adapter = citiesAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }

            searchIconButton.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
            }

            swipeRefresh.setOnRefreshListener {
                viewModel.getWeatherData()
                swipeRefresh.isRefreshing = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weatherData.collect {
                Log.i("weatherdata", "onViewCreated: $it")
                citiesAdapter.submitList(it?.list)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.homeScreenEvents.collect { event ->
                when (event) {
                    is HomeViewModel.HomeScreenEvents.ShowErrorMessage -> {
                        AlertDialog.showAlertDialog(
                            requireContext(),
                            message = event.message
                        )
                    }
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.loadingProgressBar.visibility(it)
        }
    }
}

