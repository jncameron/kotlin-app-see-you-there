package com.example.johncameron.seeyouthere.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import com.example.johncameron.seeyouthere.R

class ProfileActivity : AppCompatActivity() {

    var mCurrentUser: FirebaseUser? = null
    var mUsersDatabase: DatabaseReference? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

       // supportActionBar!!.hide()

//        supportActionBar!!.title = "Profile"
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            userId = intent.extras.get("userId").toString()

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            mUsersDatabase = FirebaseDatabase.getInstance().reference.child("Users")
                    .child(userId)


            setUpProfile()
        }

    }

    private fun setUpProfile() {

        mUsersDatabase!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                var displayName = dataSnapshot!!.child("display_name").value.toString()
                var language = dataSnapshot!!.child("language").value.toString()
                var image = dataSnapshot!!.child("image").value.toString()
                var eap = dataSnapshot!!.child("eap").value.toString()
                var studying = dataSnapshot!!.child("studying").value.toString()
                var interests = dataSnapshot!!.child("interested_in").value.toString()
                var age = dataSnapshot!!.child("age").value.toString()
                var bio = dataSnapshot!!.child("bio").value.toString()

                profileName.text = displayName
                profileAge.text = age
                profileEAP.text = eap
                profileLanguage.text = language
                profileStudying.text = studying
                profileInterests.text = interests
                profileBio.text = bio


                Picasso.with(this@ProfileActivity)
                        .load(image)
                        .placeholder(R.drawable.profile_img)
                        .into(profilePicture)


            }

            override fun onCancelled(databaseError: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
