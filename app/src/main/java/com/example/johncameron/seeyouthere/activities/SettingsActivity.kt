package com.example.johncameron.seeyouthere.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.R.id.container
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.fragments.BrowseEventsFragment
import com.example.johncameron.seeyouthere.fragments.CreateEventFragment
import com.example.johncameron.seeyouthere.fragments.MyEventsFragment
import com.example.johncameron.seeyouthere.fragments.UsersFragment
import java.io.ByteArrayOutputStream
import java.io.File

import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_user_info.view.*


class SettingsActivity : AppCompatActivity() {
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var mStorageRef: StorageReference? = null
    var GALLERY_ID: Int = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
            return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

        }

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        mStorageRef = FirebaseStorage.getInstance().reference


        var userId = mCurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userId)

        mDatabase!!.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                var displayName = dataSnapshot!!.child("display_name").value
                var image = dataSnapshot!!.child("image").value.toString()
                var userEducation = dataSnapshot!!.child("studying").value
                var userEap = dataSnapshot!!.child("eap").value
                var userLanguage = dataSnapshot!!.child("language").value
                var userAge = dataSnapshot!!.child("age").value
                var userInterests = dataSnapshot!!.child("interested_in").value
                var thumbnail = dataSnapshot!!.child("thumb_image").value

                settingsDisplayName.text = displayName.toString()
                settingsEducation.text = userEducation.toString()
                settingsEap.text = userEap.toString()
                settingsLanguage.text = userLanguage.toString()
                settingsAge.text = userAge.toString()
                settingsInterests.text = userInterests.toString()

                if (!image!!.equals("default")) {
                    Picasso.with(applicationContext)
                            .load(image)
                            .placeholder(R.drawable.profile_img)
                            .into(settingsProfileID)
                }

            }

            override fun onCancelled(databaseErrorSnapshot: DatabaseError?) {

            }

        })


        settingsChangeStatus.setOnClickListener {

            var intent = Intent(this, UserInfoActivity::class.java)
            intent.putExtra("display_name", settingsDisplayName.text.toString().trim())
            intent.putExtra("education", settingsEducation.text.toString().trim())
            intent.putExtra("eap", settingsEap.text.toString().trim())
            intent.putExtra("language", settingsLanguage.text.toString().trim())
            intent.putExtra("interests", settingsInterests.text.toString().trim())
            startActivity(intent)






        }

        settingsChangeImgBtn.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)


        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_ID
                && resultCode == Activity.RESULT_OK) {

            var image: Uri = data!!.data

            CropImage.activity(image)
                    .setAspectRatio(1, 1)
                    .start(this)

        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode === Activity.RESULT_OK) {

                 val resultUri = result.uri

                var userId = mCurrentUser!!.uid
                var thumbFile = File(resultUri.path)

                var thumbBitmap = Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(65)
                        .compressToBitmap(thumbFile)


                //We upload images to firebase
                var byteArray = ByteArrayOutputStream()
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG,  100,
                        byteArray)
                var thumbByteArray: ByteArray
                thumbByteArray = byteArray.toByteArray()

                var filePath = mStorageRef!!.child("chat_profile_images")
                        .child(userId + ".jpg")

                //Create another directory for thumbimages ( smaller, compressed images)
                var thumbFilePath = mStorageRef!!.child("chat_profile_images")
                        .child("thumbs")
                        .child(userId + ".jpg")


                filePath.putFile(resultUri)
                        .addOnCompleteListener{
                            task: Task<UploadTask.TaskSnapshot> ->
                            if (task.isSuccessful) {

                                //Let's get the pic url
                                var donwloadUrl = task.result.downloadUrl.toString()

                                //Upload Task
                                var uploadTask: UploadTask = thumbFilePath
                                        .putBytes(thumbByteArray)

                                uploadTask.addOnCompleteListener{
                                    task: Task<UploadTask.TaskSnapshot> ->
                                    var thumbUrl = task.result.downloadUrl.toString()

                                    if (task.isSuccessful) {


                                        var updateObj = HashMap<String, Any>()
                                        updateObj.put("image", donwloadUrl)
                                        updateObj.put("thumb_image", thumbUrl)

                                        //We save the profile image
                                        mDatabase!!.updateChildren(updateObj)
                                                .addOnCompleteListener {
                                                    task: Task<Void> ->
                                                     if (task.isSuccessful) {
                                                          Toast.makeText(this, "Profile Image Saved!",
                                                                  Toast.LENGTH_LONG)
                                                                  .show()

                                                     }else {

                                                     }
                                                }

                                    }else {

                                    }
                                }

                            }
                        }
            }else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d("Error", error.toString())
            }
        }
//        super.onActivityResult(requestCode, resultCode, data)
    }


}
