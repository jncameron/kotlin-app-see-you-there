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


class BrowseEventsRecyclerViewAdapter(databaseQuery: DatabaseReference, var context: Context)
    :FirebaseRecyclerAdapter<Events, BrowseEventsRecyclerViewAdapter.ViewHolder>(
        Events::class.java,
        R.layout.fragment_browseevents,
        BrowseEventsRecyclerViewAdapter.ViewHolder::class.java,
        databaseQuery){

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun populateViewHolder(browseEventsViewHolder: ViewHolder?, event: Events?, position: Int) {
        browseEventsViewHolder!!.bindView(event!!, context)
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(event: Events, context: Context) {

            var eventName = itemView.findViewById<TextView>(R.id.browseEventTitle)
            var eventHost = itemView.findViewById<TextView>(R.id.browseEventHostedBy)
            var eventLocation = itemView.findViewById<TextView>(R.id.browseEventLocation)
            var eventDetails = itemView.findViewById<TextView>(R.id.browseEventDetails)
            var eventBring = itemView.findViewById<TextView>(R.id.browseEventBring)
            var eventDate = itemView.findViewById<TextView>(R.id.browseEventDate)
            var eventTime = itemView.findViewById<TextView>(R.id.browseEventTime)
            var minAttendees = itemView.findViewById<TextView>(R.id.browseEventMin)
            var maxAttendees = itemView.findViewById<TextView>(R.id.browseEventMax)




            eventName.text = event.eventName
            eventHost.text = event.eventHost
            eventLocation.text = event.eventLocation
            eventDetails.text = event.eventDetails
            eventBring.text = event.eventBring
            eventDate.text = event.eventDate
            eventTime.text = event.eventTime
            minAttendees.text = event.minAttendees
            maxAttendees.text = event.maxAttendees


        }

    }
}
