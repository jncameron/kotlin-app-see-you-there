package com.example.johncameron.seeyouthere.adapters





import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.R.id.myEventTitle
import com.example.johncameron.seeyouthere.activities.EventActivity
import com.example.johncameron.seeyouthere.models.Events
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference


class MyEventsRecyclerViewAdapter(databaseQuery: DatabaseReference, var context: Context)
    :FirebaseRecyclerAdapter<Events, MyEventsRecyclerViewAdapter.ViewHolder>(
        Events::class.java,
        R.layout.fragment_my_events,
        MyEventsRecyclerViewAdapter.ViewHolder::class.java,
        databaseQuery){

    var mCurrentUser: FirebaseUser? = null

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun populateViewHolder(myEventsViewHolder: ViewHolder?, event: Events?, position: Int) {
        var eventId = getRef(position).key


        mCurrentUser = FirebaseAuth.getInstance().currentUser




            myEventsViewHolder!!.bindView(event!!, context)

            myEventsViewHolder.itemView.setOnClickListener {



                var eventIntent = Intent(context, EventActivity::class.java)
                eventIntent.putExtra("eventId", eventId)
                context.startActivity(eventIntent)
                //           Toast.makeText(context, eventId, Toast.LENGTH_LONG).show()
            }






    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        fun bindView(event: Events, context: Context) {

            var eventName = itemView.findViewById<TextView>(R.id.myEventTitle)
            var eventHost = itemView.findViewById<TextView>(R.id.myEventHostedBy)
            var eventLocation = itemView.findViewById<TextView>(R.id.myEventLocation)
            var eventDetails = itemView.findViewById<TextView>(R.id.myEventDetails)
            var eventBring = itemView.findViewById<TextView>(R.id.myEventBring)
            var eventDate = itemView.findViewById<TextView>(R.id.myEventDate)
            var eventTime = itemView.findViewById<TextView>(R.id.myEventTime)
            var attendees = itemView.findViewById<TextView>(R.id.myAttending)



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
