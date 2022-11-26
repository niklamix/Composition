package com.niklamix.composition.presentation.weather.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.niklamix.composition.databinding.FragmentWeatherBinding
import com.niklamix.composition.presentation.weather.DialogManager
import com.niklamix.composition.presentation.weather.WeatherViewModel
import com.niklamix.composition.presentation.weather.fragment.isPermissionGranted
import com.squareup.picasso.Picasso

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding
        get() = _binding ?: throw RuntimeException("FragmentWeatherBinding = null")

    private lateinit var pLauncher: ActivityResultLauncher<String>

    private lateinit var viewModel: WeatherViewModel

    private lateinit var fLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        fLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkPermission()
        initView()
        binding.btnUpdate.setOnClickListener {
            checkLocation()
        }

        binding.btnSearch.setOnClickListener {
            searchByNameClick()
        }

    }

    private fun searchByNameClick() {
        DialogManager.searchByNameDialog(requireContext(), object : DialogManager.Listener {
            override fun onClick(name: String?) {
                name?.let { cityName ->
                    requestWeatherData(cityName)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun permissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkLocation() {
        if (isLocationEnabled()) {
            getLocation()
        } else {
            DialogManager.locationSettingsDialog(requireContext(), object : DialogManager.Listener {
                override fun onClick(name: String?) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    }

    private fun isLocationEnabled(): Boolean {
        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLocation() {
        val token = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token.token)
            .addOnCompleteListener {
                requestWeatherData("${it.result.latitude},${it.result.longitude} ")
            }
    }

    private fun requestWeatherData(city: String) {
        val url = "https://api.weatherapi.com/v1/forecast.json?" +
                "key=$API_KEY" +
                "&q=$city" +
                "&days=1" +
                "&aqi=no" +
                "&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                viewModel.parseWeatherData(result)
            },
            { error ->
                Log.e("MyLog", "Error: $error")
            }
        )
        queue.add(request)
    }


    private fun initView() {
        with(binding) {
            viewModel.weatherCurrent.observe(viewLifecycleOwner) {
                val url = "https:${it.imageUrl}"
                val maxMinTemp = "${it.maxTemp}/${it.minTemp}"
                cityName.text = it.city
                Picasso.get().load(url).into(ivWeather)
                date.text = it.time
                tempCurrent.text = it.tempCurrent
                weatherName.text = it.condition
                tempScatter.text = maxMinTemp
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val API_KEY = "67d449f3044e4dd4846195211222511"
        private const val EXTRA_SCREEN_MODE = "extra mode"
        private const val MODE_ADD = "mode add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): WeatherFragment {
            return WeatherFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_ADD)
                }
            }
        }


    }
}