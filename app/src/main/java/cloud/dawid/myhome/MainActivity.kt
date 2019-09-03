package cloud.dawid .myhome


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.RemoteViews
import cloud.dawid.myhome.fragments.AdvancedFragment
import cloud.dawid.myhome.manager.MQTTConnectionParams
import cloud.dawid.myhome.manager.MQTTmanager
import cloud.dawid.myhome.manager.SharedPreference

import cloud.dawid.myhome.protocols.UIUpdaterInterface
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Base64
import android.widget.Toast
import cloud.dawid.myhome.data.*
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity(), UIUpdaterInterface {


    lateinit var mqttManager: MQTTmanager

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelID = "cloud.dawid.myhome"
    private val description = "MyHome notification"

    //zmienne pobierane z SharedPreferences
    var isAdvanced: Boolean = false
    lateinit var adressMQTT: String
    lateinit var usernameMQTT: String
    lateinit var passwordMQTT: String

    lateinit var domoticzOswUrl: String
    lateinit var domoticzOswLogin: String
    lateinit var domoticzOswPassword: String
    var type = "devices"

    var OpenweathermapUrl = "http://37.139.1.159/data/2.5/"


    override fun resetUIWithConnection(status: Boolean) {
    }

    override fun updateStatusViewWith(status: String) {
    }

    override fun update(message: String){
        shownotification(message)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //tworze obiekt notificationmananger na potrzeby notyfikacji
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        //ustawienie zmienny z sharedpreferences
        setVarFromSharedPreferences()

        //ustawia temperature na podstawie danych API z domoticz
        showDataFromDomoticzOswiecim()

        textTempOswiecim.setOnClickListener{
            showDataFromDomoticzOswiecim()
            showWeatherFromOpenweathermap()
            Toast.makeText(this, "odswiezam dane", Toast.LENGTH_SHORT).show()
        }


        //pokazuje pogode (ikone) na podstawie danych z Openweathermaps API
        showWeatherFromOpenweathermap()


        //laczenie z MQTT
        connectMQTT(adressMQTT, usernameMQTT, passwordMQTT)

        //fragment menager (WOL komputera)
        val fm:FragmentManager = supportFragmentManager
        val advancedFragment = AdvancedFragment()

        // fragment z przyciskami WOL do komputerów. Sprawdza czy jest ustawiona true w zmiennej isAdvanced
        if(isAdvanced!!) {
            fm.beginTransaction().add(R.id.advancedFragmentLL, advancedFragment).commit()
        }

        // przycisk ustawień, otwiera activity z ustawieniami
        settings_btn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }


//        PRZYCISKI

        btnGoraON.setOnClickListener {
            sendMessage("pin12", "false")
            //shownotification("światło góra - ON ")
        }
        btnGoraOFF.setOnClickListener {
            sendMessage("pin12", "true")
           // shownotification("światło góra - OFF ")
        }
        btnDolON.setOnClickListener {
            sendMessage("pin11", "false")
            //shownotification("światło doł - ON ")
        }
        btnDolOFF.setOnClickListener {
            sendMessage("pin11", "true")
            //shownotification("światło dol - OFF ")
        }

        btn_drzwi.setOnClickListener {
            sendMessage("opendoorintercom", "0")
           // shownotification("otwieram dzwi")
        }

        btn_alarm.setOnClickListener {
            sendMessage("alarmactivate", "0")
            //shownotification("zalaczam alarm")
        }



        val intenetSevice = Intent(this, NotificationService::class.java)
        startService(intenetSevice)

       // subscribeTopic("komunikat13")
    }


    fun setVarFromSharedPreferences(){
        val sharedPreference = SharedPreference(this)

        //MQTT
        isAdvanced = sharedPreference.getValueBoolean("advanced")!!
        adressMQTT = sharedPreference.getValueString("adresMQTT").toString()
        usernameMQTT = sharedPreference.getValueString("usernameMQTT").toString()
        passwordMQTT = sharedPreference.getValueString("passwordMQTT").toString()

        if(adressMQTT==""){
            adressMQTT = "http://127.0.0.1:1234"
            error_on_top.text = "Uzupelnij dane logowania do MQTT"
        }
        if(usernameMQTT==""){
            usernameMQTT = "error"
            error_on_top.text = "Uzupelnij dane logowania do MQTT"
        }
        if(passwordMQTT==""){
            passwordMQTT = "error"
            error_on_top.text = "Uzupelnij dane logowania do MQTT"
        }


//        //DOMOTICZ
        var serverAdresDomoticz = sharedPreference.getValueString("domoticzSerwer").toString()
        var serverPortDomoticz = sharedPreference.getValueString("domoticzPort").toString()

        if(serverAdresDomoticz=="" || serverAdresDomoticz=="null"){
            serverAdresDomoticz = "127.0.0.1"
            error_on_top.text = "Uzupelnij dane logowania do Domoticza"
        }
        if(serverPortDomoticz=="" || serverPortDomoticz=="null"){
            serverPortDomoticz = "1111"
            error_on_top.text = "Uzupelnij dane logowania do Domoticza"
        }


        domoticzOswUrl = "http://" + serverAdresDomoticz + ":" + serverPortDomoticz
        domoticzOswLogin = sharedPreference.getValueString("domoticzUsername").toString()
        domoticzOswPassword = sharedPreference.getValueString("domoticzPassword").toString()

        if(domoticzOswLogin==""){
            domoticzOswLogin = "brak"
            error_on_top.text = "Uzupelnij dane logowania do Domoticza"
        }
        if(domoticzOswPassword==""){
            domoticzOswPassword = "brak"
            error_on_top.text = "Uzupelnij dane logowania do Domoticza"
        }

    }


    fun connectMQTT(adress:String, username:String, password:String){

        var host = "tcp://" + adress + ":1883"
        var topic = "komunikacja"
        var connectionParams = MQTTConnectionParams("MQTTAndroid",host,topic,username,password)
        mqttManager = MQTTmanager(connectionParams,applicationContext,this)
        mqttManager.connect()
    }


    fun shownotification(text: String) {

        val intent = Intent(this,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)

            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelID)
                .setContentTitle("MyHome")
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.navigation_empty_icon)
        }else{
            builder = Notification.Builder(this)
                .setContentTitle("MyHome")
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.navigation_empty_icon)

        }
        notificationManager.notify(1234, builder.build())

    }


    fun sendMessage(topic:String, message: String){

        mqttManager.publish(topic, message)

    }

    fun subscribeTopic(topic:String){

        mqttManager.subscribe(topic)

    }



    private fun showDataFromDomoticzOswiecim() {

        val FUNNAME = "showDataFromDomoticzOs"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(domoticzOswUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DomoticzOswService::class.java)
        val call = service.getDevicesData(domoticzOswLogin, domoticzOswPassword, type)

        call.enqueue(object : Callback<DeviceList>{
            override fun onFailure(call: Call<DeviceList>, t: Throwable) {
                Log.e(FUNNAME, t.toString())
            }

            override fun onResponse(call: Call<DeviceList>, response: Response<DeviceList>) {
                if(response.code() == 200){
                    val devicesResponse = response.body()

                    val lastupdate = devicesResponse?.devices?.get(0)?.LastUpdate

                    val sunrise = devicesResponse?.Sunrise
                    val sunset = devicesResponse?.Sunset

                    val tempdata = devicesResponse?.devices?.get(0)?.Data

//                    ustawienie wyników w widoku:
                    textTempOswiecim.text = tempdata
                    textTempOswLastUpdate.text = lastupdate

                }
            }

        })


    }


    private fun showWeatherFromOpenweathermap(){

        val FUNNAME = "showWeatherFromO"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(OpenweathermapUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val serviceOW = retrofit.create(OpenweathermapService::class.java)
        val callOW = serviceOW.getDataFromOpenweathermap()


        callOW.enqueue(object : Callback<WeatherDataList>{
            override fun onFailure(call: Call<WeatherDataList>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<WeatherDataList>,
                response: Response<WeatherDataList>
            ) {

                val weatherDatas = response.body()

                val weatherIcon = weatherDatas?.weather?.get(0)?.icon

                val iconFileName = "ico"+weatherIcon+".png"

                iconWeatherOswiecim.setImageBitmap(getBitmapFromAssets(iconFileName))
            }

        })
    }



    private fun getBitmapFromAssets(fileName: String): Bitmap {
        val assetManager = assets
        var inputStream: InputStream? = null
        try {
            inputStream = assetManager.open("images/$fileName")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return BitmapFactory.decodeStream(inputStream)
    }

}





