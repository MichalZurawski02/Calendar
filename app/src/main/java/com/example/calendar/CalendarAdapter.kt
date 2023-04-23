package com.example.calendar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import androidx.recyclerview.widget.RecyclerView
import java.time.ZoneId
import java.time.ZonedDateTime

class CalendarAdapter(private var months: MutableList<ZonedDateTime>,  private val listener: OnDateChangeListener): RecyclerView.Adapter<CalendarAdapter.MonthsViewHolder>() {
    inner class MonthsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var calendarView : CalendarView = view.findViewById(R.id.calendarView)
        fun bind(date: ZonedDateTime, listener: OnDateChangeListener) {
            calendarView.date = date.toInstant().toEpochMilli()
            calendarView.setOnDateChangeListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.calendar_view, parent, false)
        return MonthsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MonthsViewHolder, position: Int) {
        holder.bind(months[position], listener)
    }

    override fun getItemCount(): Int {
        return months.size
    }
}