package com.example.johncameron.seeyouthere.fragments


import android.os.Bundle


import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.adapters.UsersAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_users.*




/**
 * A simple [Fragment] subclass.
 */
class UsersFragment : Fragment() {
    var mUserDatabase: DatabaseReference? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_users, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)

        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")

        userRecyclerViewId.setHasFixedSize(true)


        userRecyclerViewId.layoutManager = linearLayoutManager
        userRecyclerViewId.adapter = UsersAdapter(mUserDatabase!!, this!!.context!!)


    }

}// Required empty public constructor
