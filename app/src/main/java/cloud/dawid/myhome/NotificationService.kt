package cloud.dawid.myhome

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log


class NotificationService : IntentService("NotificationService") {

    override fun onHandleIntent(intent: Intent?) {

        for (i in 1 .. 50){
            Log.d("SERWIS", "teraz mamy numer: " + i )
            Thread.sleep(2000)
            if(i == 10){

                val CHANNEL_ID = "cloud.dawid.myhome.CHANNEL_ID"

                var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.navigation_empty_icon)
                    .setContentTitle("Title")
                    .setContentText("Powiadomienie z serwisu")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                    val notificationId = 0
                    notify(notificationId, builder.build())
                }


            }

        }

    }



}
