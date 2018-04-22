package com.example.johncameron.seeyouthere.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.fragments.BrowseEventsFragment
import com.example.johncameron.seeyouthere.fragments.CreateEventFragment
import com.example.johncameron.seeyouthere.fragments.CreateEventFragment.OnFragmentInteractionListener
import com.example.johncameron.seeyouthere.fragments.MyEventsFragment
import com.example.johncameron.seeyouthere.fragments.UsersFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity() : AppCompatActivity(),
        OnFragmentInteractionListener{
    override fun onFragmentInteraction(uri: Uri) {

    }

    val manager = supportFragmentManager
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {

                return@OnNavigationItemSelectedListener true

            }
            R.id.nav_my_events -> {
                ShowMyEventsFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_browse_events -> {
                ShowBrowseEventsFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_create_event -> {
                ShowCreateEventFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_find_users -> {
                ShowUsersFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


    }

    fun ShowUsersFragment() {

        val transaction = manager.beginTransaction()
        val fragment = UsersFragment()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.commit()
    }

    fun ShowCreateEventFragment() {
        val transaction = manager.beginTransaction()
        val fragment = CreateEventFragment()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.commit()    }

    fun ShowMyEventsFragment() {
        val transaction = manager.beginTransaction()
        val fragment = MyEventsFragment()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.commit()
    }

    fun ShowBrowseEventsFragment() {
        val transaction = manager.beginTransaction()
        val fragment = BrowseEventsFragment()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        if (item != null) {
            if (item.itemId == R.id.logoutId) {
                //Log the user out!
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            if (item.itemId == R.id.settingsId) {
                //take user to settingsActivity
                startActivity(Intent(this, SettingsActivity::class.java))

            }
        }

        return true
    }



}
