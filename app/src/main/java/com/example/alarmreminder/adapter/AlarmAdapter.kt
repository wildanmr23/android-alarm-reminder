package com.example.alarmreminder.adapter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmreminder.AlarmReceiver
import com.example.alarmreminder.databinding.ItemRowsBinding
import com.example.alarmreminder.models.AlarmModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar


class AlarmAdapter(private var supportFragmentManager: FragmentManager) :
    RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent


    class ViewHolder(val binding: ItemRowsBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root)

    private var data = ArrayList<AlarmModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.tvTime.text = item.time

        holder.binding.ivAdd.setOnClickListener {
            showTimePicker(holder, item, position)
        }

        holder.binding.ivSet.setOnClickListener {
            setAlarm(holder.context)
        }

        holder.binding.ivStop.setOnClickListener {
            stopAlarm(holder.context)
        }

    }


    private fun stopAlarm(context: Context) {
        alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Alarm Cancelled", Toast.LENGTH_SHORT).show()

    }

    private fun setAlarm(context: Context) {

        alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val id = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context, id, intent,
            0 or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )



        Toast.makeText(context, "Alarm set Successfuly", Toast.LENGTH_SHORT).show()
    }


    private fun showTimePicker(holder: ViewHolder, item: AlarmModel, position: Int) {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        picker.show(supportFragmentManager, "Alarm")

        picker.addOnPositiveButtonClickListener {

            val time: String = if (picker.hour > 12) {
                String.format("%02d", picker.hour - 12) + " : " + String.format(
                    "%02d",
                    picker.minute
                ) + " PM"
            } else {
                String.format("%02d", picker.hour) + " : " + String.format(
                    "%02d",
                    picker.minute
                ) + " AM"
            }

            holder.binding.tvTime.text = time
            item.time = time

            updateData(item, position)

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }
    }


    fun setData(data: ArrayList<AlarmModel>) {
        this.data = data
        notifyItemRangeChanged(0, data.size - 1)
    }

    fun updateData(item: AlarmModel, position: Int) {
        data[position] = item
        notifyItemChanged(position, item)
    }
}