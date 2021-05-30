package com.example.sadge

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.GeofencingEvent
import kotlin.concurrent.thread


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BroReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("f", "nibbers")
        val notificationId = 69
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "geo"
        val name: CharSequence = "Geofencing channel"
        val desc = "Used for geofencing"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(channelId, name, importance).apply {
            description = desc
            enableLights(true)
            lightColor = Color.GREEN
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            setShowBadge(true)
        }
        notificationManager.createNotificationChannel(mChannel)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentTitle("Flashback")
            .setContentText("Remember that?")


        val geoEvent = GeofencingEvent.fromIntent(intent)
        if (geoEvent.hasError()) {
            return
        }

        val trigger = geoEvent.triggeringGeofences[0].requestId
        Log.i("trig", trigger)
        thread {
            Shared.db?.pics?.selectByDate(trigger)?.let {
                val resultIntent = Intent(context, DisplayActivity::class.java)
                resultIntent.putExtra("note", it.note)
                resultIntent.putExtra("id", it.date)
                val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
                stackBuilder.addParentStack(MainActivity::class.java)
                stackBuilder.addNextIntent(resultIntent)
                val resultPendingIntent: PendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                builder.setContentIntent(resultPendingIntent)
                notificationManager.notify(notificationId, builder.build())
            }
        }
    }
}