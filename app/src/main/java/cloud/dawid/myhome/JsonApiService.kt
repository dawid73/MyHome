package cloud.dawid.myhome

import cloud.dawid.myhome.models.DomoticzOswDevices
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonApiService {
    @GET("json.htm?")
    fun getActualDataFromDevice(@Query("username") username: String, @Query("password") password: String, @Query("type") type: String): Call<DomoticzOswDevices>
}
