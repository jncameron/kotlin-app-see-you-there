package com.example.johncameron.seeyouthere.adapters





import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.activities.ChatActivity
import com.example.johncameron.seeyouthere.activities.EventActivity
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
        var eventId = getRef(position).key
        browseEventsViewHolder!!.bindView(event!!, context)

        browseEventsViewHolder.itemView.setOnClickListener {



            var eventIntent = Intent(context, EventActivity::class.java)
            eventIntent.putExtra("eventId", eventId)
            context.startActivity(eventIntent)
 //           Toast.makeText(context, eventId, Toast.LENGTH_LONG).show()
        }



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
            var attendees = itemView.findViewById<TextView>(R.id.attending)



            eventName.text = event.eventName
            eventHost.text = event.eventHost
            eventLocation.text = event.eventLocation
            eventDetails.text = event.eventDetails
            eventBring.text = event.eventBring
            eventDate.text = event.eventDate
            eventTime.text = event.eventTime
            attendees.text = event.attending?.size.toString()




        }

    }
}
