package com.example.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import java.time.ZoneId
import java.time.ZonedDateTime

class EventActivity: AppCompatActivity() {
    private lateinit var initDate: ZonedDateTime
    private lateinit var pickedDate: ZonedDateTime
    private lateinit var datePicker: DatePicker
    private lateinit var titleEdit: EditText
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_add_view)
        val day = intent.getIntExtra("Day", 0)
        val month = intent.getIntExtra("Month", 0)
        val year = intent.getIntExtra("Year", 0)
        initDate = ZonedDateTime
            .of(year, month, day, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))
        pickedDate = ZonedDateTime
            .of(year, month, day, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))
        datePicker = findViewById(R.id.datePicker1)
        titleEdit = findViewById(R.id.editTitle)
        backButton = findViewById(R.id.backView)
        datePicker.init(year, month - 1, day) { view, year, month, day ->
            pickedDate = ZonedDateTime
                .of(year, month, day, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))
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

        endActivity(view)
    }

    fun endActivity(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("Day", pickedDate.dayOfMonth)
        intent.putExtra("Month", pickedDate.monthValue)
        intent.putExtra("Year", pickedDate.year)
        startActivity(intent)
        finish()
    }
}