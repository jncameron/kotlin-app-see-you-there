package com.example.johncameron.seeyouthere.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.johncameron.seeyouthere.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_create_event.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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



    private lateinit var textViewDate: TextView
    private lateinit var textViewTime: TextView
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
            var bring = newEventBring.text.toString().trim()
            var date = newEventDate.text.toString().trim()
            var time = newEventTime.text.toString().trim()
            var min = newEventMinAttendees.text.toString().trim()
            var max = newEventMaxAttendees.text.toString().trim()

            if (!TextUtils.isEmpty(eventName) && !TextUtils.isEmpty(eventLocation)
                    && !TextUtils.isEmpty(details) && !TextUtils.isEmpty(date)
                    && !TextUtils.isEmpty(time)) {
                createEvent(eventHost, eventName, eventLocation, details, bring, date, time, min, max)

            } else {
                Toast.makeText(context, "Please fill out the fields", Toast.LENGTH_LONG)
                        .show()
            }


        }


    }

    fun createEvent(eventHost: String, eventName: String, eventLocation: String, details: String, bring: String,
                    date: String, time: String, min: String, max: String) {

        var newEvent = UUID.randomUUID().toString()

        mDatabase = FirebaseDatabase.getInstance().reference.child("Events").child(newEvent)




        var eventObject = HashMap<String, String>()
        eventObject.put("eventHost", eventHost)
        eventObject.put("eventName", eventName)
        eventObject.put("eventLocation", eventLocation)
        eventObject.put("eventDetails", details)
        eventObject.put("eventBring", bring)
        eventObject.put("eventDate", date)
        eventObject.put("eventTime", time)
        eventObject.put("minAttendees", min)
        eventObject.put("maxAttendees", max)



        mDatabase!!.setValue(eventObject)

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
    }
}
