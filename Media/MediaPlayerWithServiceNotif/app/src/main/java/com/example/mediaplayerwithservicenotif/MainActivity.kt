package com.example.mediaplayerwithservicenotif

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var mBoundServiceIntent: Intent
    private var mService: Messenger? = null
    private var mServiceBound = false
    private val mServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = Messenger(service)
            mServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            mServiceBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlay: Button = findViewById(R.id.btn_play)
        val btnStop: Button = findViewById(R.id.btn_stop)

        btnPlay.setOnClickListener{
            if (mServiceBound){
                try {
                    mService?.send(Message.obtain(null, MediaService.PLAY, 0, 0))
                }catch (e: RemoteException){
                    e.printStackTrace()
                }
            }
        }
        btnStop.setOnClickListener{
            if (mServiceBound){
                try {
                    mService?.send(Message.obtain(null, MediaService.STOP, 0, 0 ))
                }catch (e: RemoteException){
                    e.printStackTrace()
                }
            }
        }

        mBoundServiceIntent = Intent(this@MainActivity, MediaService::class.java)
        mBoundServiceIntent.action = MediaService.ACTION_CREATE
        startService(mBoundServiceIntent)
        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        unbindService(mServiceConnection)
        mBoundServiceIntent.action = MediaService.ACTION_DESTROY
        startService(mBoundServiceIntent)
    }

    companion object{
        const val TAG = "MainActivity"
    }
}