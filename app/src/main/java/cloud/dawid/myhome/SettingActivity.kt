package cloud.dawid.myhome

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val sharedPreference = SharedPreference(this)

       // sharedPreference.save("advanced", true)


        if(sharedPreference.getValueString("adres")!=null){

            var temp = sharedPreference.getValueString("adres")

            Log.d("ADRESS TUTAJ: ", temp)

            adress_properties.setText(temp)

            switchadvaced.isChecked = sharedPreference.getValueBoolean("advanced")!!
        }



        btn_sava.setOnClickListener{
            val adresString = adress_properties.editableText.toString()

            var isAdwance = switchadvaced.isChecked

            Toast.makeText(this@SettingActivity, isAdwance.toString(), Toast.LENGTH_SHORT).show()

            sharedPreference.save("advanced", isAdwance)
            sharedPreference.save("adres", adresString)


        }



    }
}
