package cloud.dawid.myhome.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cloud.dawid.myhome.R
import cloud.dawid.myhome.manager.MQTTmanager
import kotlinx.android.synthetic.main.fragment_advanced.*


class AdvancedFragment : Fragment() {

    lateinit var mqttManager: MQTTmanager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advanced, container, false)

        btn_wol.setOnClickListener {
            sendMessage("testowo1", "0")
        }

    }

    fun sendMessage(topic:String, message: String){

        mqttManager.publish(topic, message)

    }

}
