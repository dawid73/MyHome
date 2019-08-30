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


        if(sharedPreference.getValueString("adresMQTT")!=null){

            adress_properties.setText(sharedPreference.getValueString("adresMQTT"))
            user_properties.setText(sharedPreference.getValueString("usernameMQTT"))
            password_properties.setText(sharedPreference.getValueString("passwordMQTT"))

            switchadvaced.isChecked = sharedPreference.getValueBoolean("advanced")!!
        }



        btn_sava.setOnClickListener{
            var adresMQTTString = adress_properties.editableText.toString()
            var usernameMQTTString = user_properties.editableText.toString()
            var passwordMQTTString = password_properties.editableText.toString()

            var isAdwance = switchadvaced.isChecked

            Toast.makeText(this@SettingActivity, isAdwance.toString(), Toast.LENGTH_SHORT).show()

            sharedPreference.save("advanced", isAdwance)
            sharedPreference.save("adresMQTT", adresMQTTString)
            sharedPreference.save("usernameMQTT", usernameMQTTString)
            sharedPreference.save("passwordMQTT", passwordMQTTString)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



    }
}
