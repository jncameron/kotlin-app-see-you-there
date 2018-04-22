package com.example.johncameron.seeyouthere.adapters





import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.R.id.myEventTitle
import com.example.johncameron.seeyouthere.models.Events
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference


class MyEventsRecyclerViewAdapter(databaseQuery: DatabaseReference, var context: Context)
    :FirebaseRecyclerAdapter<Events, MyEventsRecyclerViewAdapter.ViewHolder>(
        Events::class.java,
        R.layout.fragment_my_events,
        MyEventsRecyclerViewAdapter.ViewHolder::class.java,
        databaseQuery){

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun populateViewHolder(myEventsViewHolder: ViewHolder?, event: Events?, position: Int) {
        myEventsViewHolder!!.bindView(event!!, context)
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(event: Events, context: Context) {

            var eventName = itemView.findViewById<TextView>(R.id.myEventTitle)
            var eventLocation = itemView.findViewById<TextView>(R.id.myEventLocation)
            var eventDetails = itemView.findViewById<TextView>(R.id.myEventDetails)
            var eventBring = itemView.findViewById<TextView>(R.id.myEventBring)
            var eventDate = itemView.findViewById<TextView>(R.id.myEventDate)
            var eventTime = itemView.findViewById<TextView>(R.id.myEventTime)
            var minAttendees = itemView.findViewById<TextView>(R.id.myEventMin)





            eventName.text = event.eventName
            eventLocation.text = event.eventLocation
            eventDetails.text = event.eventDetails
            eventBring.text = event.eventBring
            eventDate.text = event.eventDate
            eventTime.text = event.eventTime
//            minAttendees.text = event.attending


        }

    }
}
