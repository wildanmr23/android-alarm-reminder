package com.example.alarmreminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmreminder.adapter.AlarmAdapter
import com.example.alarmreminder.databinding.ActivityMainBinding
import com.example.alarmreminder.repository.Repository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var alarmAdapter: AlarmAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initial()
        showData()
        checkPermissions()
        createNotificationChannel()
        binding.tvTime.text = getCurrentDate()
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun initial() {
        alarmAdapter = AlarmAdapter(supportFragmentManager)
    }

    private fun showData() {
        val data = Repository.getDataAlarm()
        alarmAdapter.setData(data)

        binding.rvMainCategory.layoutManager = LinearLayoutManager(this)
        binding.rvMainCategory.adapter = alarmAdapter
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100
                )
            }
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name: CharSequence = "AndroidReminder"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alarmId", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


}