package com.example.johncameron.seeyouthere.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_info.*
import com.example.johncameron.seeyouthere.R
import org.w3c.dom.Text

class UserInfoActivity : AppCompatActivity() {
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null

    lateinit var options: Spinner
    lateinit var result: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        //supportActionBar!!.title = "Update Details"

        var oldDisplayName = intent.extras.get("display_name")
        displayNameUpdateEt.setHint(oldDisplayName.toString())

        var oldStatus = intent.extras.get("status")
        statusUpdateEt.setHint(oldStatus.toString())

        var oldCountry = intent.extras.get("country")
        countryUpdateEt.setHint(oldCountry.toString())

        var oldEap = intent.extras.get("eap")
        eapUpdateEt.setHint(oldEap.toString())



        displayNameUpdateBtn.setOnClickListener {


            mCurrentUser = FirebaseAuth.getInstance().currentUser
            var userId = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(userId)

            var mDisplayName = displayNameUpdateEt.text.toString().trim()

            mDatabase!!.child("display_name")
                    .setValue(mDisplayName).addOnCompleteListener { task: Task<Void> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Display Name Updated Successfully!", Toast.LENGTH_LONG)
                                    .show()
                            startActivity(Intent(this, SettingsActivity::class.java))

                        } else {

                            Toast.makeText(this, "Display Name Not Updated!", Toast.LENGTH_LONG)
                                    .show()

                        }
                    }

        }

        statusUpdateBtn.setOnClickListener {


            mCurrentUser = FirebaseAuth.getInstance().currentUser
            var userId = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(userId)

            var status = statusUpdateEt.text.toString().trim()

            mDatabase!!.child("status")
                    .setValue(status).addOnCompleteListener { task: Task<Void> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Status Updated Successfully!", Toast.LENGTH_LONG)
                                    .show()
                            startActivity(Intent(this, SettingsActivity::class.java))

                        } else {

                            Toast.makeText(this, "Status Not Updated!", Toast.LENGTH_LONG)
                                    .show()

                        }
                    }

        }

        val eapLevels = arrayOf("EAP 1", "EAP 2", "EAP 3", "EAP 4", "EAP 5")

        options = findViewById(R.id.eapSpinner)
        //result = findViewById(R.id.settingsEap)
        options.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eapLevels)

        options.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
             //   result.text = "Please select a level"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                eapUpdateEt.text = eapLevels.get(position)
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
                    .setValue(eap).addOnCompleteListener { task: Task<Void> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "EAP Level Updated Successfully!", Toast.LENGTH_LONG)
                                    .show()
                            startActivity(Intent(this, SettingsActivity::class.java))

                        } else {

                            Toast.makeText(this, "EAP Level Not Updated!", Toast.LENGTH_LONG)
                                    .show()

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
                        .setValue(country).addOnCompleteListener { task: Task<Void> ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Country Updated Successfully!", Toast.LENGTH_LONG)
                                        .show()
                                startActivity(Intent(this, SettingsActivity::class.java))

                            } else {

                                Toast.makeText(this, "Country Not Updated!", Toast.LENGTH_LONG)
                                        .show()

                            }
                        }

            }

        }
    }
}

