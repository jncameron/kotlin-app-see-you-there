package com.example.johncameron.seeyouthere.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_info.*
import com.example.johncameron.seeyouthere.R

class UserInfoActivity : AppCompatActivity() {
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        supportActionBar!!.title = "Update Details"

        var oldStatus = intent.extras.get("status")
        statusUpdateEt.setHint(oldStatus.toString())

        var oldCountry = intent.extras.get("country")
        countryUpdateEt.setHint(oldCountry.toString())

        var oldEap = intent.extras.get("eap")
        eapUpdateEt.setHint(oldEap.toString())

        statusUpdateBtn.setOnClickListener {


            mCurrentUser = FirebaseAuth.getInstance().currentUser
            var userId = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(userId)

            var status = statusUpdateEt.text.toString().trim()

            mDatabase!!.child("status")
                    .setValue(status).addOnCompleteListener {
                        task: Task<Void> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Status Updated Successfully!", Toast.LENGTH_LONG)
                                    .show()
                            startActivity(Intent(this, SettingsActivity::class.java))

                        }else {

                            Toast.makeText(this, "Status Not Updated!", Toast.LENGTH_LONG)
                                    .show()

                        }
                    }

        }

        eapUpdateBtn.setOnClickListener {


            mCurrentUser = FirebaseAuth.getInstance().currentUser
            var userId = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(userId)

            var eap = eapUpdateEt.text.toString().trim()

            mDatabase!!.child("eap")
                    .setValue(eap).addOnCompleteListener {
                        task: Task<Void> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "EAP Level Updated Successfully!", Toast.LENGTH_LONG)
                                    .show()
                            startActivity(Intent(this, SettingsActivity::class.java))

                        }else {

                            Toast.makeText(this, "EAP Level Not Updated!", Toast.LENGTH_LONG)
                                    .show()

                        }
                    }

        }

        countryUpdateBtn.setOnClickListener {


            mCurrentUser = FirebaseAuth.getInstance().currentUser
            var userId = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(userId)

            var country = countryUpdateEt.text.toString().trim()

            mDatabase!!.child("country")
                    .setValue(country).addOnCompleteListener {
                        task: Task<Void> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Country Updated Successfully!", Toast.LENGTH_LONG)
                                    .show()
                            startActivity(Intent(this, SettingsActivity::class.java))

                        }else {

                            Toast.makeText(this, "Country Not Updated!", Toast.LENGTH_LONG)
                                    .show()

                        }
                    }

        }

//        if (intent.extras != null) {
//            var oldEap = intent.extras.get("eap")
//            statusUpdateEt.setHint(oldEap.toString())
//        }
//        if (intent.extras.equals(null)) {
//            statusUpdateEt.setText("Enter Your EAP Level")
//        }




    }
}
