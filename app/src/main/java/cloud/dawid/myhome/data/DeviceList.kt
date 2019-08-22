package cloud.dawid.myhome.data

import com.google.gson.annotations.SerializedName

class DeviceList {
    @SerializedName("result")
    var devices: List<Devices>? = null
    var Sunrise: String = ""
    var Sunset: String = ""
}

class Devices{
    var HardwareName: String = ""
    var LastUpdate: String = ""
    var Data: String = ""
    var Name: String = ""

}