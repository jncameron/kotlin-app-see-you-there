package com.example.johncameron.seeyouthere.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.adapters.MyEventsRecyclerViewAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_my_events_list.*


class MyEventsFragment : Fragment() {
    var mEventDatabase: DatabaseReference? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_my_events_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)

        mEventDatabase = FirebaseDatabase.getInstance().reference.child("Events")


      //  myEventRecycler.setHasFixedSize(true)

        myEventRecycler.layoutManager = linearLayoutManager
        myEventRecycler.adapter = MyEventsRecyclerViewAdapter(mEventDatabase!!, this.context!!)
    }


}
