package cloud.dawid.myhome

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import cloud.dawid.myhome.manager.SharedPreference
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val sharedPreference = SharedPreference(this)

        // USTAWIENIE AKTUALNYCH USTAWIEN:

        //MQTT Server
        adress_properties.setText(sharedPreference.getValueString("adresMQTT"))
        user_properties.setText(sharedPreference.getValueString("usernameMQTT"))
        password_properties.setText(sharedPreference.getValueString("passwordMQTT"))
        switchadvaced.isChecked = sharedPreference.getValueBoolean("advanced")!!

        //domoticz Server
        domitczSerwer.setText(sharedPreference.getValueString("domoticzSerwer"))
        domitczPort.setText(sharedPreference.getValueString("domoticzPort"))
        domoticzUsername.setText(sharedPreference.getValueString("domoticzUsername"))
        domoticzPassword.setText(sharedPreference.getValueString("domoticzPassword"))



        btn_save.setOnClickListener{

            var adresMQTTString = adress_properties.editableText.toString()
            var usernameMQTTString = user_properties.editableText.toString()
            var passwordMQTTString = password_properties.editableText.toString()
            var isAdwance = switchadvaced.isChecked

            var domitczSerwerString = domitczSerwer.editableText.toString()
            var domoticzPortString = domitczPort.editableText.toString()
            var domoticzUsernameString = domoticzUsername.editableText.toString()
            var domoticzPasswordString = domoticzPassword.editableText.toString()

            sharedPreference.save("advanced", isAdwance)
            sharedPreference.save("adresMQTT", adresMQTTString)
            sharedPreference.save("usernameMQTT", usernameMQTTString)
            sharedPreference.save("passwordMQTT", passwordMQTTString)

            sharedPreference.save("domoticzSerwer", domitczSerwerString)
            sharedPreference.save("domoticzPort", domoticzPortString)
            sharedPreference.save("domoticzUsername", domoticzUsernameString)
            sharedPreference.save("domoticzPassword", domoticzPasswordString)

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }



    }
}
