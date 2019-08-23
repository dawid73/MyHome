package cloud.dawid.myhome.data

import com.google.gson.annotations.SerializedName

class WeatherForecastList {

    @SerializedName("list")
    var forecastList: List<Forecast>? = null
    var dt_txt: String = ""

}

class Forecast{
    var weather: List<WeatherForecast>? = null
}

class WeatherForecast{
    var icon: String = ""
}
