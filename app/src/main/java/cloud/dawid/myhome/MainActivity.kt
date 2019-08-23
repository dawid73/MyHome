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


    override fun resetUIWithConnection(status: Boolean) {

    }

    override fun updateStatusViewWith(status: String) {

    }

    override fun update(message: String){

        shownotification(message)

    }


    override fun onCreate(savedInstanceState: Bundle?) {


        showDataFromDomoticzOswiecim()
        showWeatherFromOpenweathermap()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val fm:FragmentManager = supportFragmentManager
        val advancedFragment = AdvancedFragment()

        val sharedPreference = SharedPreference(this)

        var isAdvanced = sharedPreference.getValueBoolean("advanced")
        var adress = sharedPreference.getValueString("adres")

        connect(adress.toString())

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
            sendMessage("pin12", "1")
            shownotification("światło góra - ON ")
        }
        btnGoraOFF.setOnClickListener {
            sendMessage("pin12", "0")
            shownotification("światło góra - OFF ")
        }
        btnDolON.setOnClickListener {
            sendMessage("pin11", "1")
            shownotification("światło doł - ON ")
        }
        btnDolOFF.setOnClickListener {
            sendMessage("pin11", "0")
            shownotification("światło dol - OFF ")
        }

        btn_drzwi.setOnClickListener {
            sendMessage("opendoorintercom", "0")
            shownotification("otwieram dzwi")
        }

        btn_alarm.setOnClickListener {
            sendMessage("alarmactivate", "0")
            shownotification("zalaczam alarm")
        }



        val intenetSevice = Intent(this, NotificationService::class.java)
        startService(intenetSevice)

       // subscribeTopic("komunikat13")
    }




    fun connect(adress:String){

        if (true) {

            var host = "tcp://" + adress + ":1883"
            var topic = "komunikat11"
            var connectionParams = MQTTConnectionParams("MQTTSample",host,topic,"myhome","Oswiecim")
            mqttManager = MQTTmanager(connectionParams,applicationContext,this)
            mqttManager.connect()

        }else{

            updateStatusViewWith("Please enter all valid fields")

        }

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
            .baseUrl(DomoticzOswUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DomoticzOswService::class.java)
        val call = service.getDevicesData()

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

        val service = retrofit.create(OpenweathermapService::class.java)
        val call = service.getDataFromOpenweathermap()

        call.enqueue(object : Callback<WeatherDataList>{
            override fun onFailure(call: Call<WeatherDataList>, t: Throwable) {
                Log.e(FUNNAME, t.toString())
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

    private fun showForecastFromOpen(){
        val FUNNAME = "showForecastFromOpen"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(OpenweathermapUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OpenweathermapService::class.java)
        val call = service.getForecastFromOpenweathermap()

        call.enqueue(object : Callback<WeatherForecastList>{
            override fun onFailure(call: Call<WeatherForecastList>, t: Throwable) {
                Log.e(FUNNAME, t.toString())
            }

            override fun onResponse(
                call: Call<WeatherForecastList>,
                response: Response<WeatherForecastList>
            ) {
                val weatherDatas = response.body()

                //TODO tutaj dopisz kod który pokazuje prognozę pogody
            }

        })

    }




    companion object {

        var BaseUrl = "http://188.117.181.24:4444/"
        var login = "Y3p1am5paw=="
        var password = "T3N3aWVjaW0zMjYwMA=="
        var type = "devices"
        var DomoticzOswUrl = "http://188.117.181.24:4444"
        var OpenweathermapUrl = "http://api.openweathermap.org/data/2.5/"
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





