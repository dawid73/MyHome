package cloud.dawid.myhome.models

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class DomoticzOswDevices {
    @SerializedName("result")
    var result: List<Device>? = null
}

class Device {
    val data: String = ""
}