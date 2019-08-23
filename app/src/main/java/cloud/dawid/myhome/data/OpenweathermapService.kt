package cloud.dawid.myhome.data

import retrofit2.Call
import retrofit2.http.GET

interface OpenweathermapService {
    @GET("weather?q=Oswiecim,pl&appid=6666d79db1d616f6887ae7145de05782&units=metric")
    fun getDataFromOpenweathermap(): Call<WeatherDataList>

    @GET("forecast?q=Oswiecim,pl&appid=6666d79db1d616f6887ae7145de05782&units=metric")
    fun getForecastFromOpenweathermap(): Call<WeatherForecastList>
}