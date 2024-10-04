package com.example.androidtaskmaria.presentation.detailscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.androidtaskmaria.R
import com.example.androidtaskmaria.databinding.FragmentDetailScreenBinding
import com.example.androidtaskmaria.domain.model.WeatherInfo
import com.example.androidtaskmaria.presentation.AlertDialog
import com.example.androidtaskmaria.presentation.visibility
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailScreenFragment : Fragment() {
    private lateinit var binding: FragmentDetailScreenBinding
    private val viewModel: DetailsScreenViewModel by viewModels()
    private lateinit var weatherData: WeatherInfo.WeatherData
    private val args by navArgs<DetailScreenFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherData = args.weatherItem
        binding.apply {
            cityTextView.text = weatherData.name
            feelsLikeValue.text = getString(
                R.string.feels_like_value,
                Math.round(weatherData.main.feelLike).toString()
            )
            humidityPercent.text = getString(R.string.humidity_format, weatherData.main.humidity)
            pressureValue.text = getString(R.string.pressure_format, weatherData.main.pressure)
            windSpeedValue.text = (weatherData.wind.speed).toString()
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewModel.getWeatherInfoDaily(weatherData.name)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weatherDataDaily.collect { weatherData ->
                Log.i("weatherdata", "onViewCreated: $weatherData")

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