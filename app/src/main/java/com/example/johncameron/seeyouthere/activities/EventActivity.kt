package com.example.johncameron.seeyouthere.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.johncameron.seeyouthere.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_profile.*

class EventActivity : AppCompatActivity() {
    var mCurrentUser: FirebaseUser? = null
    var mDatabase: DatabaseReference? = null

    var eventId: String? = null
    var userId1: String? = null
    var userId2: String? = null
    var userId3: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

   //     if (intent.extras != null) {

            eventId = intent.extras.get("eventId").toString()

            mCurrentUser = FirebaseAuth.getInstance().currentUser

            mDatabase = FirebaseDatabase.getInstance().reference

//        }

        setUpEvent()



    }

    fun setUpEvent() {
        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                var eventName = dataSnapshot!!.child("Events").child(eventId).child("eventName").value.toString()
                var eventHost = dataSnapshot!!.child("Events").child(eventId).child("eventHost").value.toString()
                var eventLocation = dataSnapshot!!.child("Events").child(eventId).child("eventLocation").value.toString()

                var attendingUsers: DataSnapshot = dataSnapshot!!.child("Events").child(eventId).child("attending")

                var attendingUsersList = arrayListOf<String>()

                for (user in attendingUsers.children) {
                    attendingUsersList?.add(user.value.toString())

                }

                Toast.makeText(applicationContext, attendingUsersList.toString(), Toast.LENGTH_LONG).show()


                if (attendingUsersList.size == 1) {

                    userId1 = attendingUsersList[0]
                    andOthers.visibility = View.INVISIBLE
                    eventAttendingUserProfile2.visibility = View.INVISIBLE
                    eventAttendingUserProfile3.visibility = View.INVISIBLE
                    var imageUrl1 = dataSnapshot!!.child("Users").child(userId1).child("thumb_image")?.value.toString()

                    Picasso.with(applicationContext).load(imageUrl1)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile1)

                } else if (attendingUsersList.size == 2) {

                    userId1 = attendingUsersList[0]
                    userId2 = attendingUsersList[1]
                    andOthers.visibility = View.INVISIBLE
                    eventAttendingUserProfile3.visibility = View.INVISIBLE

                    var imageUrl1 = dataSnapshot!!.child("Users").child(userId1).child("thumb_image")?.value.toString()
                    var imageUrl2 = dataSnapshot!!.child("Users").child(userId2).child("thumb_image")?.value.toString()

                    Picasso.with(applicationContext).load(imageUrl1)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile1)

                    Picasso.with(applicationContext).load(imageUrl2)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile2)
                } else if (attendingUsersList.size == 3) {

                    userId1 = attendingUsersList[0]
                    userId2 = attendingUsersList[1]
                    userId3 = attendingUsersList[2]
                    andOthers.visibility = View.INVISIBLE

                    var imageUrl1 = dataSnapshot!!.child("Users").child(userId1).child("thumb_image")?.value.toString()
                    var imageUrl2 = dataSnapshot!!.child("Users").child(userId2).child("thumb_image")?.value.toString()
                    var imageUrl3 = dataSnapshot!!.child("Users").child(userId3).child("thumb_image")?.value.toString()

                    Picasso.with(applicationContext).load(imageUrl1)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile1)

                    Picasso.with(applicationContext).load(imageUrl2)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile2)

                    Picasso.with(applicationContext).load(imageUrl3)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile3)

                } else {

                    userId1 = attendingUsersList[0]
                    userId2 = attendingUsersList[1]
                    userId3 = attendingUsersList[2]

                    var imageUrl1 = dataSnapshot!!.child("Users").child(userId1).child("thumb_image")?.value.toString()
                    var imageUrl2 = dataSnapshot!!.child("Users").child(userId2).child("thumb_image")?.value.toString()
                    var imageUrl3 = dataSnapshot!!.child("Users").child(userId3).child("thumb_image")?.value.toString()

                    Picasso.with(applicationContext).load(imageUrl1)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile1)

                    Picasso.with(applicationContext).load(imageUrl2)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile2)

                    Picasso.with(applicationContext).load(imageUrl3)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile3)

                }




                var attendingUserName = dataSnapshot!!.child("Users").child(userId1).child("display_name")?.value.toString()


                singleEventTitle.text = eventName
                singleEventHostedBy.text = eventHost
                singleEventLocation.text = eventLocation





                if (!attendingUsersList.contains(mCurrentUser!!.uid)) {
                    iWillAttend.visibility = View.VISIBLE

                    iWillAttend.setOnClickListener {
                        mDatabase!!.child("Events").child(eventId).child("attending").push().setValue(mCurrentUser!!.uid)
                    }

                } else {
                    iWillAttend.visibility = View.INVISIBLE
                }

            }
//


            override fun onCancelled(databaseError: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }) 
    }
}
