package cloud.dawid.myhome

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val sharedPreference:SharedPreference=SharedPreference(this)


        if(sharedPreference.getValueString("adres")!=null){


            adress_properties.hint = sharedPreference.getValueString("adres")!!

        }else{

            btn_sava.setOnClickListener{
                val adresString = adress_properties.editableText.toString()
                sharedPreference.save("adres", adresString)
            }
        }


    }
}
