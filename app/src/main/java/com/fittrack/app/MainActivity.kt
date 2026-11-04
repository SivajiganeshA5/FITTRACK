package com.fittrack.app
import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import android.webkit.*
import android.widget.Toast
class MainActivity:Activity(){
 private lateinit var web:WebView
 private val rx=object:BroadcastReceiver(){override fun onReceive(c:Context?,i:Intent?){val km=i?.getDoubleExtra("km",0.0)?:0.0;val st=i?.getIntExtra("steps",0)?:0;web.evaluateJavascript("onNativeUpdate($km,$st)",null)}}
 @Suppress("SetJavaScriptEnabled") override fun onCreate(b:Bundle?){super.onCreate(b);web=WebView(this).apply{settings.javaScriptEnabled=true;settings.domStorageEnabled=true;addJavascriptInterface(Bridge(),"FitTrackNative");webViewClient=WebViewClient()};setContentView(web);val f=IntentFilter(TrackingService.UPDATE);if(Build.VERSION.SDK_INT>=33)registerReceiver(rx,f,RECEIVER_NOT_EXPORTED) else @Suppress("DEPRECATION") run{registerReceiver(rx,f)};web.loadUrl("file:///android_asset/index.html")}
 inner class Bridge{@JavascriptInterface fun startTracking(t:String){runOnUiThread{if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),9);Toast.makeText(this@MainActivity,"Grant location permission, then tap Start again.",Toast.LENGTH_LONG).show()}else{val i=Intent(this@MainActivity,TrackingService::class.java).putExtra("type",t);if(Build.VERSION.SDK_INT>=26)startForegroundService(i) else startService(i)}}}
 @JavascriptInterface fun setPaused(p:Boolean){startService(Intent(this@MainActivity,TrackingService::class.java).putExtra("paused",p))}
 @JavascriptInterface fun stopTracking(){startService(Intent(this@MainActivity,TrackingService::class.java).setAction("STOP"))}}
 override fun onDestroy(){try{unregisterReceiver(rx)}catch(_:Exception){};super.onDestroy()}
}