package com.example.myservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyForegroundService : Service() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not Yet Implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildNotification()

        startForeground(NOTIFICATION_ID, notification)
        Log.d(TAG, "Service dijalankan...")
        serviceScope.launch {
            for (i in 1..50){
                delay(1000)
                Log.d(TAG, "Do something $i")
            }
            stopForeground(true)
            stopSelf()
            Log.d(TAG, "Service dihentikan...")
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        Log.d(TAG, "onDestroy: Service dihentikan...")
    }

    private fun buildNotification(): Notification{
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingFlags: Int = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, pendingFlags)
        val mNotificationManaget = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Saat ini foreground service sedang berjalan")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_NAME
            notificationBuilder.setChannelId(CHANNEL_ID)
            mNotificationManaget.createNotificationChannel(channel)
        }

        return notificationBuilder.build()
    }

    companion object{
        internal val TAG = MyForegroundService::class.java.simpleName

        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "dicoding channel"
    }
}