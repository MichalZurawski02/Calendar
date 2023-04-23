package com.example.calendar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.room.RoomDatabase
import java.time.ZoneId
import java.time.ZonedDateTime

class EventActivity: AppCompatActivity() {
    private lateinit var initDate: ZonedDateTime
    private lateinit var pickedDate: ZonedDateTime
    private lateinit var datePicker: DatePicker
    private lateinit var titleEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_add_view)
        var day = intent.getIntExtra("Day", 0)
        var month = intent.getIntExtra("Month", 0)
        var year = intent.getIntExtra("Year", 0)
        initDate = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))
        Log.i("init", initDate.toString())
        pickedDate = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))
        datePicker = findViewById<DatePicker>(R.id.datePicker1)
        titleEdit = findViewById<EditText>(R.id.editTitle)
        datePicker.init(year, month - 1, day) { view, year, month, day ->
            pickedDate = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))
            Log.i("picked", pickedDate.toString())
        }
    }

    fun eventAdd(view: View) {
        if(titleEdit.text.toString() == "") {
            Toast.makeText(this@EventActivity, "Title cannot be empty", Toast.LENGTH_LONG).show()
            return
        }

        val event = Event(0, titleEdit.text.toString(), pickedDate)
        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "Calendar"
        ).allowMainThreadQueries().addTypeConverter(ZonedDateTimeConverter()).build()
        db.eventDao().insert(event)

        var intent = Intent(this, MainActivity::class.java)
        intent.putExtra("Day", pickedDate.dayOfMonth)
        intent.putExtra("Month", pickedDate.monthValue)
        intent.putExtra("Year", pickedDate.year)
        startActivity(intent)
        finish()
    }
}