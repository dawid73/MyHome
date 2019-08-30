package cloud.dawid.myhome.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DomoticzOswService {
    @GET("json.htm?")
    fun getDevicesData(@Query("username") username: String, @Query("password") password: String, @Query("type") type: String): Call<DeviceList>

}

