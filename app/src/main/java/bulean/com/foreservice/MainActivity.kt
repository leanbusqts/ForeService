package bulean.com.foreservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtView: TextView = findViewById(R.id.txtView)
        /*
        // Start service at application start
        if (!FService.IS_RUNNING) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(applicationContext, FService::class.java))
            } else {
                startService(Intent(applicationContext, FService::class.java))
            }
        }
        */

        // Turn on Service
        val btnStart: Button = findViewById(R.id.start_service)
        btnStart.setOnClickListener(View.OnClickListener {
            FService.strtService(this)
            txtView.text = getString(R.string.service_on)
        })
        // Turn off Service
        val btnStop: Button = findViewById(R.id.stop_service)
        btnStop.setOnClickListener(View.OnClickListener {
            FService.stpService(this)
            txtView.text = getString(R.string.service_off)
        })
    }
}
