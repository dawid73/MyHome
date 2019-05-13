package cloud.dawid.myhome

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import cloud.dawid.myhome.manager.MQTTConnectionParams
import cloud.dawid.myhome.manager.MQTTmanager
import cloud.dawid.myhome.protocols.UIUpdaterInterface
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient

class MainActivity : Activity(), UIUpdaterInterface {

    var mqttManager: MQTTmanager? = null

    // Interface methods
    override fun resetUIWithConnection(status: Boolean) {

//        ipAddressField.isEnabled  = !status
//        topicField.isEnabled      = !status
//        messageField.isEnabled    = status
//        connectBtn.isEnabled      = !status
//        sendBtn.isEnabled         = status
//
//        // Update the status label.
//        if (status){
//            updateStatusViewWith("Connected")
//        }else{
//            updateStatusViewWith("Disconnected")
//        }
    }

    override fun updateStatusViewWith(status: String) {
//        statusLabl.text = status
    }

    override fun update(message: String) {
//
//        var text = messageHistoryView.text.toString()
//        var newText = """
//            $text
//            $message
//            """
//        //var newText = text.toString() + "\n" + message +  "\n"
//        messageHistoryView.setText(newText)
//        messageHistoryView.setSelection(messageHistoryView.text.length)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connect()

        btn_alarm.setOnClickListener { sendMessage() }

    }

    fun connect(){

        //if (!(ipAddressField.text.isNullOrEmpty() && topicField.text.isNullOrEmpty())) {
            //var host = "tcp://" + ipAddressField.text.toString() + ":1883"
            var host = "tcp://188.117.181.24:1883"
            //var topic = topicField.text.toString()
            var topic = "test1"
            var connectionParams = MQTTConnectionParams("MQTTSample",host,topic,"","")
            mqttManager = MQTTmanager(connectionParams,applicationContext,this)
            mqttManager?.connect()
//        }else{
//            updateStatusViewWith("Please enter all valid fields")
//        }

    }

    fun sendMessage(){

        mqttManager?.publish("Test11")


    }


}


