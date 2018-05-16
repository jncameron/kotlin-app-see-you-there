package com.example.johncameron.seeyouthere.activities

import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.johncameron.seeyouthere.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_profile.*

class EventActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var mCurrentUser: FirebaseUser? = null
    var mDatabase: DatabaseReference? = null

    var eventId: String? = null
    var userId1: String? = null
    var userId2: String? = null
    var userId3: String? = null
    var userId4: String? = null
    var eventLocation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
//        val mapView = eventMapView
//        mapView.getMapAsync(this)
//        mapView.onCreate(savedInstanceState)



   //     if (intent.extras != null) {

            eventId = intent.extras.get("eventId").toString()

            mCurrentUser = FirebaseAuth.getInstance().currentUser

            mDatabase = FirebaseDatabase.getInstance().reference

//        }


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.eventMapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)




    }

    override fun onMapReady(googleMap: GoogleMap) {


        mMap = googleMap


        setUpEvent()
        // Add a marker in Sydney and move the camera


    }

    fun setUpMap() {

    }

    fun setUpEvent() {
        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                var eventName = dataSnapshot!!.child("Events").child(eventId).child("eventName").value.toString()
                var eventImage = dataSnapshot!!.child("Events").child(eventId).child("eventImage").value.toString()
                var eventDetails = dataSnapshot!!.child("Events").child(eventId).child("eventDetails").value.toString()
                eventLocation = dataSnapshot!!.child("Events").child(eventId).child("eventLocation").value.toString()

                val lat: Double
                val long: Double
                var geocodeMatches = Geocoder(applicationContext).getFromLocationName(eventLocation, 1)

                lat = geocodeMatches[0].latitude
                long = geocodeMatches[0].longitude


                val sydney = LatLng(lat, long)
                mMap.addMarker(MarkerOptions().position(sydney).title(eventLocation))
                //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f))


                var attendingUsers: DataSnapshot = dataSnapshot!!.child("Events").child(eventId).child("attending")

                var attendingUsersList = arrayListOf<String>()

                for (user in attendingUsers.children) {
                    attendingUsersList?.add(user.value.toString())

                }

                Picasso.with(this@EventActivity)
                        .load(eventImage)
                        .placeholder(R.drawable.echopoint)
                        .into(heroEventImage)

                //find users with same language, EAP Level, education, intestest
                userId1 = attendingUsersList[0]
                var eapLevel = dataSnapshot!!.child("Users").child(mCurrentUser!!.uid).child("eap")?.value.toString()

                var sameEap = 0
                for (user in attendingUsersList) {
                    if (dataSnapshot!!.child("Users").child(user).child("eap").value.toString() == eapLevel) {
                        sameEap += 1
                    }
                }

                EapLevelAttendingNumber.text = sameEap.toString()
                eapLevel = eapLevel + " Students"
                EapLevelAttendingText.text = eapLevel




                var language = dataSnapshot!!.child("Users").child(mCurrentUser!!.uid).child("language")?.value.toString()
                var sameLanguage = 0
                for (user in attendingUsersList) {
                    if (dataSnapshot!!.child("Users").child(user).child("language").value.toString() == language) {
                        sameLanguage += 1
                    }
                }
                languageAttendingNumber.text = sameLanguage.toString()

                language = language + " Speakers"
                languageAttendingText.text = language

                var education = dataSnapshot!!.child("Users").child(mCurrentUser!!.uid).child("studying")?.value.toString()
                var sameEducation = 0
                for (user in attendingUsersList) {
                    if (dataSnapshot!!.child("Users").child(user).child("studying").value.toString() == education) {
                        sameEducation += 1
                    }
                }
                EducationAttendingNumber.text = sameEducation.toString()
                education = education + " Students"
                EducationAttendingText.text = education

                var interests = dataSnapshot!!.child("Users").child(mCurrentUser!!.uid).child("interested_in")?.value.toString()
                var sameInterests = 0
                for (user in attendingUsersList) {
                    if (dataSnapshot!!.child("Users").child(user).child("interested_in").value.toString() == interests) {
                        sameInterests += 1
                    }
                }
                InterestsAttendingNumber.text = sameInterests.toString()
                interests = "Love \n$interests!"
                InterestsAttendingText.text = interests



                if (attendingUsersList.size == 1) {

                    userId1 = attendingUsersList[0]
                    andOthers.visibility = View.INVISIBLE
                    eventAttendingUserProfile2.visibility = View.INVISIBLE
                    eventAttendingUserProfile3.visibility = View.INVISIBLE
                    eventAttendingUserProfile4.visibility = View.INVISIBLE
                    var imageUrl1 = dataSnapshot!!.child("Users").child(userId1).child("thumb_image")?.value.toString()

                    Picasso.with(applicationContext).load(imageUrl1)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile1)

                } else if (attendingUsersList.size == 2) {

                    userId1 = attendingUsersList[0]
                    userId2 = attendingUsersList[1]
                    andOthers.visibility = View.INVISIBLE
                    eventAttendingUserProfile3.visibility = View.INVISIBLE
                    eventAttendingUserProfile4.visibility = View.INVISIBLE

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
                    eventAttendingUserProfile4.visibility = View.INVISIBLE
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

                } else if (attendingUsersList.size == 4) {

                    userId1 = attendingUsersList[0]
                    userId2 = attendingUsersList[1]
                    userId3 = attendingUsersList[2]
                    userId4 = attendingUsersList[3]
                    andOthers.visibility = View.INVISIBLE

                    var imageUrl1 = dataSnapshot!!.child("Users").child(userId1).child("thumb_image")?.value.toString()
                    var imageUrl2 = dataSnapshot!!.child("Users").child(userId2).child("thumb_image")?.value.toString()
                    var imageUrl3 = dataSnapshot!!.child("Users").child(userId3).child("thumb_image")?.value.toString()
                    var imageUrl4 = dataSnapshot!!.child("Users").child(userId4).child("thumb_image")?.value.toString()

                    Picasso.with(applicationContext).load(imageUrl1)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile1)

                    Picasso.with(applicationContext).load(imageUrl2)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile2)

                    Picasso.with(applicationContext).load(imageUrl3)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile3)

                    Picasso.with(applicationContext).load(imageUrl4)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile4)

                }

                else {

                    userId1 = attendingUsersList[0]
                    userId2 = attendingUsersList[1]
                    userId3 = attendingUsersList[2]
                    userId4 = attendingUsersList[3]

                    var othersAttending = attendingUsersList.size - 4
                    var othersAttendingTxt = "+$othersAttending"

                    andOthers.text = othersAttendingTxt

                    var imageUrl1 = dataSnapshot!!.child("Users").child(userId1).child("thumb_image")?.value.toString()
                    var imageUrl2 = dataSnapshot!!.child("Users").child(userId2).child("thumb_image")?.value.toString()
                    var imageUrl3 = dataSnapshot!!.child("Users").child(userId3).child("thumb_image")?.value.toString()
                    var imageUrl4 = dataSnapshot!!.child("Users").child(userId4).child("thumb_image")?.value.toString()

                    Picasso.with(applicationContext).load(imageUrl1)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile1)

                    Picasso.with(applicationContext).load(imageUrl2)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile2)

                    Picasso.with(applicationContext).load(imageUrl3)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile3)

                    Picasso.with(applicationContext).load(imageUrl4)
                            .placeholder(R.drawable.profile_img)
                            .into(eventAttendingUserProfile4)

                }




                var attendingUserName = dataSnapshot!!.child("Users").child(userId1).child("display_name")?.value.toString()


                singleEventTitle.text = eventName
                singleEventLocation.text = eventLocation
                singleEventDetails.text = eventDetails





                if (!attendingUsersList.contains(mCurrentUser!!.uid)) {
                    iWillAttend.visibility = View.VISIBLE
                    isAttending.visibility = View.INVISIBLE

                    iWillAttend.setOnClickListener {
                        mDatabase!!.child("Events").child(eventId).child("attending").push().setValue(mCurrentUser!!.uid)
                    }

                } else {
                    iWillAttend.visibility = View.INVISIBLE
                    isAttending.visibility = View.VISIBLE
                }

            }
//


            override fun onCancelled(databaseError: DatabaseError?) {

            }
        })


    }
}
