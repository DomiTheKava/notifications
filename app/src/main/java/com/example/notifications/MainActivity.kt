package com.example.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager

    private var channelID = "com.example.notifications"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // service
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

//        val areNotifsEnabled = areNoticationsEnabled(this, channelID)

        val btn1: Button = findViewById<Button>(R.id.button)

        btn1.setOnClickListener {

            if (!areNoticationsEnabled(this, channelID)) {

                val snackbar = Snackbar.make(it, "You need to enable notifications for this app", Snackbar.LENGTH_LONG)
                snackbar.setAction("Open Settings", View.OnClickListener {
                    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                })
                snackbar.show()
            } else {
                sendNotification("Example Notify", "this is the example notification content!")

            }
        }

        // default channel
        createNotificationChannel(channelID, "Local Notify Default")

    }

    private fun sendNotification(title: String, content: String) {
        val notificationId = 100
        val icon = Icon.createWithResource(this, androidx.constraintlayout.widget.R.drawable.notification_tile_bg)
        val resultIntent = Intent(this, ResultActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
            PendingIntent.FLAG_IMMUTABLE)
        val action: Notification.Action = Notification.Action.Builder(icon, "Open", pendingIntent).build()
        val notification = Notification.Builder(this, channelID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(icon)
            .setColor(Color.CYAN)
            .setContentIntent(pendingIntent)
            .setActions(action)
            .setNumber(notificationId)
            .build()

        notificationManager.notify(notificationId, notification)
    }


    fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance).apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100,200,300)
        }

        notificationManager?.createNotificationChannel(channel)

    }

    fun areNoticationsEnabled(context: Context, channelId: String?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !TextUtils.isEmpty(channelId)) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = manager.getNotificationChannel(channelId)
            channel != null && channel.importance != NotificationManager.IMPORTANCE_NONE
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

}

