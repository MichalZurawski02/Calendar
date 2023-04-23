package com.example.calendar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.recyclerview.widget.RecyclerView
import java.time.ZoneId
import java.time.ZonedDateTime

class CalendarAdapter(private var months: MutableList<ZonedDateTime>): RecyclerView.Adapter<CalendarAdapter.MonthsViewHolder>() {
    inner class MonthsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var calendarView : CalendarView = view.findViewById(R.id.calendarView)
        fun bind(date: ZonedDateTime) {
            calendarView.date = date.toInstant().toEpochMilli()
//            calendarView.setOnDateChangeListener(
//                CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
//                    Log.i("1", "Year=" + year + " Month=" + month + " Day=" + dayOfMonth);
//                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.calendar_view, parent, false)
        return MonthsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MonthsViewHolder, position: Int) {
        holder.bind(months[position])
    }

    override fun getItemCount(): Int {
        return months.size
    }
}