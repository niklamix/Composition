package com.niklamix.composition.presentation.weather

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.niklamix.composition.presentation.weather.fragment.isPermissionGranted
import com.niklamix.composition.presentation.weather.model.WeatherItem
import org.json.JSONObject

class WeatherViewModel : ViewModel() {
    private val _weatherCurrent = MutableLiveData<WeatherItem>()
    val weatherCurrent: LiveData<WeatherItem>
        get() = _weatherCurrent
    private val _isPermissionGranted = MutableLiveData<Boolean>()
    val isPermissionGranted: LiveData<Boolean>
        get() = _isPermissionGranted



    fun parseWeatherData(result: String) {
        val mainObject = JSONObject(result)
        val item = initWeatherItem(mainObject)
        _weatherCurrent.value = item
    }

    private fun initWeatherItem(mainObject: JSONObject): WeatherItem {
        val city = mainObject.getJSONObject("location").getString("name")
        val time = mainObject.getJSONObject("current").getString("last_updated")
        val conditionText = mainObject.getJSONObject("current")
            .getJSONObject("condition").getString("text")
        val icon = mainObject.getJSONObject("current")
            .getJSONObject("condition").getString("icon")
        val currentTemp = mainObject.getJSONObject("current").getString("temp_c")
        val maxTemp = JSONObject(
            mainObject.getJSONObject("forecast")
                .getJSONArray("forecastday")[0].toString()
        )
            .getJSONObject("day").getString("maxtemp_c")
        val minTemp = JSONObject(
            mainObject.getJSONObject("forecast")
                .getJSONArray("forecastday")[0].toString()
        )
            .getJSONObject("day").getString("mintemp_c")
        return WeatherItem(city, time, conditionText, icon, currentTemp, maxTemp, minTemp)
    }
}