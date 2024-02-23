package com.example.alarmreminder.repository

import android.os.FileUtils
import com.example.alarmreminder.models.AlarmModel

object Repository {

    fun  getDataAlarm(): ArrayList<AlarmModel>{
        val data = ArrayList<AlarmModel>()
        data.add(AlarmModel("-- : --", false))
        data.add(AlarmModel("-- : --", false))
        data.add(AlarmModel("-- : --", false))
        data.add(AlarmModel("-- : --", false))
        data.add(AlarmModel("-- : --", false))
        data.add(AlarmModel("-- : --", false))
        data.add(AlarmModel("-- : --", false))

        return data

    }
}