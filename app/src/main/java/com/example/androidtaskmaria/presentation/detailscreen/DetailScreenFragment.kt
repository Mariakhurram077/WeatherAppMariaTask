package com.example.androidtaskmaria.presentation.detailscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtaskmaria.R
import com.example.androidtaskmaria.databinding.FragmentDetailScreenBinding
import com.example.androidtaskmaria.domain.model.WeatherInfo
import com.example.androidtaskmaria.presentation.AlertDialog
import com.example.androidtaskmaria.presentation.detailscreen.adapter.WeatherAdapter
import com.example.androidtaskmaria.presentation.isDaytime
import com.example.androidtaskmaria.presentation.toWeatherIcon
import com.example.androidtaskmaria.presentation.visibility
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailScreenFragment : Fragment() {
    private lateinit var binding: FragmentDetailScreenBinding
    private val viewModel: DetailsScreenViewModel by viewModels()
    private lateinit var weatherData: WeatherInfo.WeatherData
    private val args by navArgs<DetailScreenFragmentArgs>()
    private lateinit var weatherInfoAdapter: WeatherAdapter
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
        weatherInfoAdapter = WeatherAdapter()
        val currentTime = System.currentTimeMillis() / 1000
        val isDay = currentTime.isDaytime(
            sunrise = weatherData.sys.sunrise,
            sunset = weatherData.sys.sunset
        )
        val iconResId = weatherData.weather[0].main.toWeatherIcon(isDay = isDay)
        binding.apply {
            cityTextView.text = weatherData.name
            feelsLikeValue.text = getString(
                R.string.feels_like_value,
                Math.round(weatherData.main.feelLike).toString()
            )
            humidityPercent.text = getString(R.string.humidity_format, weatherData.main.humidity)
            pressureValue.text = getString(R.string.pressure_format, weatherData.main.pressure)
            windSpeedValue.text = (weatherData.wind.speed).toString()
            weatherIv.setImageResource(iconResId)

            backButton.setOnClickListener {
                findNavController().navigateUp()
            }

            rvWeather.apply {
                adapter = weatherInfoAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }

        viewModel.getWeatherInfoDaily(weatherData.name)
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