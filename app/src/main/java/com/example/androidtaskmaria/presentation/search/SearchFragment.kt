package com.example.androidtaskmaria.presentation.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtaskmaria.databinding.FragmentSearchBinding
import com.example.androidtaskmaria.presentation.AlertDialog
import com.example.androidtaskmaria.presentation.detailscreen.DetailsScreenViewModel
import com.example.androidtaskmaria.presentation.detailscreen.adapter.WeatherAdapter
import com.example.androidtaskmaria.presentation.visibility
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: DetailsScreenViewModel by viewModels()
    private lateinit var weatherInfoAdapter: WeatherAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherInfoAdapter = WeatherAdapter()
        binding.apply {
            searchIcon.setOnClickListener {
                val cityName = searchEditText.text.toString()
                if (cityName.isNotBlank()) {
                    viewModel.getWeatherInfoDaily(cityName = cityName)
                } else {
                    Toast.makeText(requireContext(), "Please Enter City Name!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            rvSearchWeather.apply {
                adapter = weatherInfoAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weatherDataDaily.collect { weatherData ->
                viewModel.groupWeatherByDay(
                    list = weatherData?.list ?: emptyList(),
                    weatherData?.city?.sunrise ?: 0,
                    weatherData?.city?.sunset ?: 0
                )
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weatherData.collect {
                weatherInfoAdapter.submitList(it)

            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.detailScreenEvents.collect { event ->
                when (event) {
                    is DetailsScreenViewModel.DetailScreenEvents.ShowErrorMessage -> {
                        AlertDialog.showAlertDialog(requireContext(), message = event.message)
                    }
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.loadingProgressBar.visibility(it)
        }
    }
}