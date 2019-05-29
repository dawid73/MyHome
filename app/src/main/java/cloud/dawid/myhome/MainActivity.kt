package cloud.dawid.myhome


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import cloud.dawid.myhome.fragments.AdvancedFragment
import cloud.dawid.myhome.manager.MQTTConnectionParams
import cloud.dawid.myhome.manager.MQTTmanager
import cloud.dawid.myhome.manager.SharedPreference
import cloud.dawid.myhome.protocols.UIUpdaterInterface
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UIUpdaterInterface {

    var mqttManager: MQTTmanager? = null

    override fun resetUIWithConnection(status: Boolean) {

    }

    override fun updateStatusViewWith(status: String) {

    }

    override fun update(message: String) {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm:FragmentManager = supportFragmentManager
        val advancedFragment = AdvancedFragment()

        val sharedPreference = SharedPreference(this)

        var isAdvanced = sharedPreference.getValueBoolean("advanced")
        var adress = sharedPreference.getValueString("adres")

        connect(adress.toString())

        //subscribe("test2")

        // fragment z przyciskami WOL do komputerów. Sprawdza czy jest ustawiona true w zmiennej isAdvanced
        if(isAdvanced!!) {
            fm.beginTransaction().add(R.id.advancedFragmentLL, advancedFragment).commit()
        }

        // przycisk ustawień
        settings_btn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        //
        btn_drzwi.setOnClickListener {
            sendMessage("opendoorintercom", "0")
        }

    }


    fun connect(adress:String){

        if (true) {

            var host = "tcp://" + adress + ":1883"
            var topic = "test1"
            var connectionParams = MQTTConnectionParams("MQTTSample",host,topic,"","")
            mqttManager = MQTTmanager(connectionParams,applicationContext,this)
            mqttManager?.connect()

        }else{

            updateStatusViewWith("Please enter all valid fields")

        }

    }


    fun sendMessage(topic:String, message: String){

        mqttManager?.publish(topic, message)

    }

    fun subscribe(topic:String){
        mqttManager?.subscribe("test2")
    }

}


