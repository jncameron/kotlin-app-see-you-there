package com.example.johncameron.seeyouthere.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.adapters.BrowseEventsRecyclerViewAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_browseevents_list.*



class BrowseEventsFragment : Fragment() {
    var mEventDatabase: DatabaseReference? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_browseevents_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)

        mEventDatabase = FirebaseDatabase.getInstance().reference.child("Events")


        browseEventsRecycler.layoutManager = linearLayoutManager
        browseEventsRecycler.adapter = BrowseEventsRecyclerViewAdapter(mEventDatabase!!, this.context!!)
    }

}

