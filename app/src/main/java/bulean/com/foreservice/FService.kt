package bulean.com.foreservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class FService : Service(){

    // https://developer.android.com/reference/kotlin/android/app/Service
    // https://developer.android.com/guide/components/services

    companion object {
        const val TAG = "FOREGROUND_SERVICE"

        //const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        //const val KEY_DATA = "KEY_DATA"

        private const val CHANNEL_ID: String = "1234"
        private const val CHANNEL_NAME: String = "CHANNEL"

        private const val SERVICE_ID: Int = 1

        var IS_RUNNING: Boolean = false

        // Function to Start and Stop Service from MainActivity
        fun strtService(context: Context) {
            // https://developer.android.com/guide/components/services#StartingAService
            val startIntent = Intent(context, FService::class.java)
            context.startForegroundService(startIntent)
            IS_RUNNING = true
        }
        fun stpService(context: Context){
            // https://developer.android.com/guide/components/services#Stopping
            val stopIntent = Intent(context, FService::class.java)
            context.stopService(stopIntent)
            IS_RUNNING = false
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "ON START COMMAND")
        /*if (intent != null) {
            when (intent.action) {
                ACTION_STOP_FOREGROUND_SERVICE -> stopService()
                //ACTION_OPEN_APP -> openAppHomePage(intent.getStringExtra(KEY_DATA))
            }
        }*/
        return START_STICKY;
    }

    /*private fun openAppHomePage(value: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra(KEY_DATA, value)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }*/

    // NotificationChannel
    // https://developer.android.com/reference/kotlin/android/app/NotificationChannel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)

            nChannel.lightColor = Color.BLUE
            nChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(nChannel)
        }
    }

    // To build and start foreground service.
    private fun startForegroundService() {
        //Create Notification channel for all the notifications sent from this app.
        createNotificationChannel()
        startFS()
    }

    private fun startFS() {
        val description = getString(R.string.description_notification)
        val title = String.format(getString(R.string.title_notification), getString(R.string.app_name))
        // Start ForegroundService
        // https://developer.android.com/reference/kotlin/android/app/Service#startforeground
        startForeground(SERVICE_ID, getStickyNotification(title, description))
        IS_RUNNING = true
    }

    // Create Notification
    // https://developer.android.com/reference/kotlin/android/app/Notification.Builder
    // https://developer.android.com/training/notify-user/build-notification
    private fun getStickyNotification(title: String, message: String): Notification? {
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, Intent(), 0)

        // Create notification builder.
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        builder.setContentTitle(title)
        builder.setContentText(message)
        /* // Make notification show big text.
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(title)
        bigTextStyle.bigText(message)
        builder.setStyle(bigTextStyle)
        */
        //builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.drawable.ic_notification)
        //val largeIconBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_alarm_on)
        //builder.setLargeIcon(largeIconBitmap)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true)

       /* // Add 'OpenApp' button in notification.
        val openAppIntent = Intent(applicationContext, FService::class.java)
        openAppIntent.action = ACTION_OPEN_APP
        openAppIntent.putExtra(KEY_DATA, "" + System.currentTimeMillis())
        val pendingPlayIntent = PendingIntent.getService(applicationContext, 0, openAppIntent, 0)
        val openAppAction = NotificationCompat.Action(
            android.R.drawable.ic_menu_view,
            getString(R.string.lbl_btn_sticky_notification_open_app),
            pendingPlayIntent)
        builder.addAction(openAppAction)
        */
        // Build the notification.
        return builder.build()
    }

    override fun onDestroy() {
        IS_RUNNING = false
    }
}