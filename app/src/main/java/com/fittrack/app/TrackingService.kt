package com.fittrack.app
import android.app.*
import android.content.Intent
import android.location.*
import android.os.*
import android.hardware.*
import android.content.Context
class TrackingService:Service(),LocationListener,SensorEventListener{
 private lateinit var lm:LocationManager;private var sm:SensorManager?=null;private var stepSensor:Sensor?=null;private var steps=0;private var meters=0.0;private var last:Location?=null;private var paused=false
 override fun onCreate(){super.onCreate();lm=getSystemService(LOCATION_SERVICE) as LocationManager;sm=getSystemService(Context.SENSOR_SERVICE) as SensorManager;stepSensor=sm?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)}
 override fun onStartCommand(i:Intent?,f:Int,id:Int):Int{if(i?.action=="STOP"){stopNow();return START_NOT_STICKY};if(i?.hasExtra("paused")==true){paused=i.getBooleanExtra("paused",false);if(paused)last=null else requestGps();return START_STICKY};val ch=NotificationChannel("fittrack","Workout tracking",NotificationManager.IMPORTANCE_LOW);getSystemService(NotificationManager::class.java).createNotificationChannel(ch);val n=Notification.Builder(this,"fittrack").setContentTitle("FITTRACK workout active").setContentText("Recording your active session").setSmallIcon(android.R.drawable.ic_menu_mylocation).build();startForeground(7,n);requestGps();stepSensor?.let{sm?.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL)};return START_STICKY}
 private fun requestGps(){try{if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000L,2f,this)}catch(_:SecurityException){}}
 override fun onLocationChanged(l:Location){if(paused)return;last?.let{val d=it.distanceTo(l);if(d in 1f..100f)meters+=d};last=l;sendUpdate()}
 override fun onSensorChanged(e:SensorEvent){if(!paused&&e.sensor.type==Sensor.TYPE_STEP_DETECTOR){steps++;sendUpdate()}}
 override fun onAccuracyChanged(s:Sensor?,a:Int){}
 private fun sendUpdate(){sendBroadcast(Intent(UPDATE).setPackage(packageName).putExtra("km",meters/1000).putExtra("steps",steps))}
 private fun stopNow(){lm.removeUpdates(this);sm?.unregisterListener(this);stopForeground(STOP_FOREGROUND_REMOVE);stopSelf()}
 override fun onDestroy(){try{lm.removeUpdates(this)}catch(_:Exception){};sm?.unregisterListener(this);super.onDestroy()}
 override fun onBind(i:Intent?)=null
 companion object{const val UPDATE="com.fittrack.app.TRACK_UPDATE"}
}