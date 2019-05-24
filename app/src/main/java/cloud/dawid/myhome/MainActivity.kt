package cloud.dawid.myhome


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import cloud.dawid.myhome.manager.MQTTConnectionParams
import cloud.dawid.myhome.manager.MQTTmanager
import cloud.dawid.myhome.protocols.UIUpdaterInterface
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_advanced.*
import org.eclipse.paho.android.service.MqttAndroidClient

class MainActivity : AppCompatActivity(), UIUpdaterInterface {


    private var EMPTY = ""
    private var ADRESS = "Adres serwera"
    private var USER = "Użytkownik"
    private var PASS = "Hasło"

    private var myPreferences = "myPref"




    override fun resetUIWithConnection(status: Boolean) {

    }

    private val showadwance: Boolean = true
    var mqttManager: MQTTmanager? = null



    override fun updateStatusViewWith(status: String) {

    }

    override fun update(message: String) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm:FragmentManager = supportFragmentManager
        val advancedFragment = AdvancedFragment()

        if(showadwance) {
            fm.beginTransaction().add(R.id.advancedFragmentLL, advancedFragment).commit()
        }

        connect()
        btn_alarm.setOnClickListener { sendMessage()

        }

        btn_drzwi.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

    }

    fun connect(){

        val sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)

        val addres = sharedPreferences.getString(ADRESS, EMPTY)
        val user = sharedPreferences.getString(USER, EMPTY)
        val pass = sharedPreferences.getString(PASS, EMPTY)

        if (!(addres.isNullOrEmpty() && user.isNullOrEmpty() && pass.isNullOrEmpty())) {
            var host = "tcp://" + addres + ":1883"
            //var host = "tcp://188.117.181.24:1883"
            //var topic = topicField.text.toString()
            var topic = "test1"
            var connectionParams = MQTTConnectionParams("MQTTSample",host,topic,"","")
            mqttManager = MQTTmanager(connectionParams,applicationContext,this)
            mqttManager?.connect()
        }else{
            updateStatusViewWith("Please enter all valid fields")
        }

        }


    fun sendMessage(){

        mqttManager?.publish("0")


    }


}


