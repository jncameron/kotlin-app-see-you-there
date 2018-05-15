package com.example.johncameron.seeyouthere.adapters





import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.activities.ChatActivity
import com.example.johncameron.seeyouthere.activities.EventActivity
import com.example.johncameron.seeyouthere.models.Events
import com.example.johncameron.seeyouthere.models.Users
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso


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
            var eventLocation = itemView.findViewById<TextView>(R.id.browseEventLocation)
            var eventDate = itemView.findViewById<TextView>(R.id.browseEventDate)
            var eventTime = itemView.findViewById<TextView>(R.id.browseEventTime)
            var attendees = itemView.findViewById<TextView>(R.id.attending)
        //    var eventImage = itemView.findViewById<ImageView>(R.id.browseEventImage)




            eventName.text = event.eventName
            eventLocation.text = event.eventLocation
            eventDate.text = event.eventDate
            eventTime.text = event.eventTime
            attendees.text = event.attending?.size.toString()
            Picasso.with(context).load(event.eventImage).into(itemView.findViewById<ImageView>(R.id.browseEventImage))

          //  eventImage.setImageURI(Uri.parse(event.eventImage))


        }

    }
}
