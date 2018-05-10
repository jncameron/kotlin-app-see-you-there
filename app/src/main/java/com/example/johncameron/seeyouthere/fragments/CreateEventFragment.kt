package com.example.johncameron.seeyouthere.fragments

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.activities.Main2Activity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_create_event.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.example.johncameron.seeyouthere.activities.MainActivity
import com.google.android.gms.location.places.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CreateEventFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CreateEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CreateEventFragment : Fragment() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mGeoDataClient: GeoDataClient? = null
    val PLACE_PICKER_REQUEST = 1




    private lateinit var textViewDate: TextView
    private lateinit var textViewTime: TextView
    private lateinit var textViewLocation: TextView
    private lateinit var textViewPhoto: TextView
    private lateinit var imageView: ImageView
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_create_event, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var cal = Calendar.getInstance()
        var eventHost = ""

        textViewTime = newEventTime
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            val timeFormatter = SimpleDateFormat("HH:mm").format(cal.time)
            val sb = StringBuilder()

            sb.append((timeFormatter))
            textViewTime.text = sb
        }

        textViewTime.setOnClickListener {
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }


        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(FragmentActivity(), null)
                .build()

        textViewLocation = newEventLocation
        textViewLocation.setOnClickListener {

            var builder = PlacePicker.IntentBuilder()
            try {
                startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }

        }


        textViewPhoto = newEventPhoto
        textViewPhoto.setOnClickListener {
            getPhotos()
        }


        textViewDate = newEventDate

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        var userId = mCurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userId)

        mDatabase!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                eventHost = dataSnapshot!!.child("display_name").value.toString()
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateFormatter = SimpleDateFormat("dd.MM.yyyy").format(cal.time)
            val sb = StringBuilder()
            sb.append(dateFormatter)

            textViewDate.text = sb

        }



        textViewDate.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }



        accountCreateEventBtn.setOnClickListener {

            var eventName = newEventName.text.toString().trim()
            var eventLocation = newEventLocation.text.toString().trim()
            var details = newEventDetails.text.toString().trim()
            var date = newEventDate.text.toString().trim()
            var time = newEventTime.text.toString().trim()
            var attend = userId.toString()

            if (!TextUtils.isEmpty(eventName) && !TextUtils.isEmpty(eventLocation)
                    && !TextUtils.isEmpty(details) && !TextUtils.isEmpty(date)
                    && !TextUtils.isEmpty(time)) {
                createEvent(eventHost, eventName, eventLocation, details, date, time, attend)

            } else {
                Toast.makeText(context, "Please fill out the fields", Toast.LENGTH_LONG)
                        .show()
            }


        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    //    super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(context, data!!)
                val stBuilder = StringBuilder()
                val placename = String.format("%s", place.getName())
                val latitude = place.getLatLng().latitude
                val longitude = place.getLatLng().longitude
                val address = String.format("%s", place.getAddress())
                stBuilder.append(address)
                var addressTxt = stBuilder.toString()
                addressTxt = address.substringBefore("NSW")
                textViewLocation.text = addressTxt
        }

    }

    fun createEvent(eventHost: String, eventName: String, eventLocation: String, details: String,
                    date: String, time: String, attend: String) {

        var newEvent = UUID.randomUUID().toString()

        mDatabase = FirebaseDatabase.getInstance().reference.child("Events").child(newEvent)




        var eventObject = HashMap<String, String>()
        eventObject.put("eventHost", eventHost)
        eventObject.put("eventName", eventName)
        eventObject.put("eventLocation", eventLocation)
        eventObject.put("eventDetails", details)
        eventObject.put("eventDate", date)
        eventObject.put("eventTime", time)
 //       eventObject.put("attending", attend)



        mDatabase!!.setValue(eventObject)
        mDatabase!!.child("attending").push().setValue(attend)

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri)
    }

    private fun getPhotos() {
        Toast.makeText(context, "Hi Photo", Toast.LENGTH_SHORT).show()
        val placeId = "ChIJa147K9HX3IAR-lwiGIQv9i4"
        val photoMetadataResponse = mGeoDataClient!!.getPlacePhotos(placeId)
        photoMetadataResponse?.addOnCompleteListener(object : OnCompleteListener<PlacePhotoMetadataResponse> {
            override fun onComplete(@NonNull task: Task<PlacePhotoMetadataResponse>) {
                // Get the list of photos.
                val photos = task.getResult()
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                val photoMetadataBuffer = photos.getPhotoMetadata()
                // Get the first photo in the list.
                val photoMetadata = photoMetadataBuffer.get(0)
                // Get the attribution text.
                val attribution = photoMetadata.getAttributions()
                // Get a full-size bitmap for the photo.
                val photoResponse = mGeoDataClient!!.getPhoto(photoMetadata)
                photoResponse?.addOnCompleteListener(object : OnCompleteListener<PlacePhotoResponse> {
                    override fun onComplete(@NonNull task: Task<PlacePhotoResponse>) {
                        val photo = task.getResult()
                        val bitmap = photo.getBitmap()
                        imageView.setImageBitmap(bitmap)
                    }
                })
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateEventFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CreateEventFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        fun create(): MainActivity = MainActivity()


    }
}
