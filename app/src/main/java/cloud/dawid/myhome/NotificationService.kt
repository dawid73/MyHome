package cloud.dawid.myhome

import android.app.*
import android.content.Intent
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log


class NotificationService : IntentService("NotificationService") {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelID = "cloud.dawid.myhome"
    private val description = "MyHome notification"

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    }

    override fun onHandleIntent(intent: Intent?) {


    }


}
