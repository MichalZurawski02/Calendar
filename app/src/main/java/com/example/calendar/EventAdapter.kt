package com.example.calendar

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private var events: List<Event>): RecyclerView.Adapter<EventAdapter.EventsViewHolder>() {
    inner class EventsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var textView : TextView = view.findViewById(R.id.titleTextView)
        fun bind(event: Event) {
            textView.text = event.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.EventsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_view, parent, false)
        return EventsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventAdapter.EventsViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEventList(events: List<Event>) {
        this.events = events
        Log.i("", events.toString())
        notifyDataSetChanged()
    }
}