package cloud.dawid.myhome

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SettingActivity : AppCompatActivity() {

    private var EMPTY = ""
    private var ADRESS = "Adres serwera"
    private var USER = "Użytkownik"
    private var PASS = "Hasło"

    private var myPreferences = "myPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val sharePreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)


        if(sharePreferences.getString(ADRESS, EMPTY) != EMPTY){

        }


    }
}
