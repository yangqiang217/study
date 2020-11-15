package com.yq.kotlindemo.domain

import com.yq.kotlindemo.Forecast

data class ForecastList(val city: String, val country: String, val dailyForecast: List<Forecast>)
data class Forecast(val date: String, val description: String, val high: Int, val low: Int)