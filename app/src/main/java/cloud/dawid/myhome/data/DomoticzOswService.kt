package cloud.dawid.myhome.data

import retrofit2.Call
import retrofit2.http.GET

interface DomoticzOswService {
    @GET("json.htm?username=Y3p1am5paw==&password=T3N3aWVjaW0zMjYwMA==&type=devices")
    fun getDevicesData(): Call<DeviceList>

}
