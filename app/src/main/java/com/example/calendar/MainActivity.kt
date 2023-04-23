package com.example.calendar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import java.time.ZoneId
import java.time.ZonedDateTime

class MainActivity : AppCompatActivity() {
    private lateinit var calendarRV: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventsRV: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private var linearLayoutManager = LinearLayoutManager(this)
    private var linearLayoutManager2 = LinearLayoutManager(this)
    private var date: ZonedDateTime = ZonedDateTime.now()
    var list = ArrayList<ZonedDateTime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "Calendar"
        ).allowMainThreadQueries().addTypeConverter(ZonedDateTimeConverter()).build()

        if (intent != null) {
            val day = intent.getIntExtra("Day", 0)
            val month = intent.getIntExtra("Month", 0)
            val year = intent.getIntExtra("Year", 0)
            if (day != 0) {
                date = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))
            }
        }

        calendarRV = findViewById(R.id.calendarRV)
        LinearSnapHelper().attachToRecyclerView(calendarRV)
        calendarRV.layoutManager = linearLayoutManager
        calendarAdapter = CalendarAdapter(generateInit(date),
            CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            val clickedDate = ZonedDateTime
                .of(year, month + 1, dayOfMonth, 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))
            eventAdapter.setEventList(db.eventDao().getByDateTime(clickedDate.toLocalDate().atStartOfDay(clickedDate.zone)))
            })
        calendarRV.scrollToPosition(list.size/2)
        calendarRV.adapter = calendarAdapter

        eventsRV = findViewById(R.id.eventsRV)
        eventsRV.layoutManager = linearLayoutManager2
        eventAdapter = EventAdapter(db.eventDao().getByDateTime(date.toLocalDate().atStartOfDay(date.zone)))
        eventsRV.adapter = eventAdapter

        calendarRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val pastItemsVisible = linearLayoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastItemsVisible >= totalItemCount) {
                        list.add(list[list.size - 1].plusMonths(1))
                        calendarAdapter.notifyItemInserted(list.size - 1)
                    }
                }
                if (dy < 0) {
                    val pastItemsVisible = linearLayoutManager.findFirstVisibleItemPosition()
                    if (pastItemsVisible == 0) {
                        list.add(0, list[0].minusMonths(1))
                        calendarAdapter.notifyItemInserted(0)
                    }
                }
            }
        })
    }

    companion object {
        const val DAY = "day"
        const val MONTH = "month"
        const val YEAR = "year"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(DAY, date.dayOfMonth)
            putInt(MONTH, date.monthValue)
            putInt(YEAR, date.year)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.run {
            date = ZonedDateTime.of(getInt(YEAR), getInt(MONTH), getInt(DAY),
                0, 0, 0, 0, ZoneId.of("Europe/Warsaw"))
        }
    }

    private fun generateInit(date: ZonedDateTime): MutableList<ZonedDateTime>  {
        for(i in 6 downTo 0) {
            list.add(date.minusMonths(i.toLong()))
        }
        for(i in 1..6){
            list.add(date.plusMonths(i.toLong()))
        }
        return list
    }

    fun onAddClick(view: View) {
        val intent = Intent(this, EventActivity::class.java)
        val x = linearLayoutManager.findFirstVisibleItemPosition()
        intent.putExtra("Day", list[x].dayOfMonth)
        intent.putExtra("Month", list[x].monthValue)
        intent.putExtra("Year", list[x].year)
        Log.i("wtf", list[x].dayOfMonth.toString() + " " + list[x].monthValue.toString() + " " + list[x].year.toString())
        startActivity(intent)
        finish()
    }
}