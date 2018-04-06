package com.example.johncameron.seeyouthere.adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.example.johncameron.seeyouthere.fragments.ChatsFragment
import com.example.johncameron.seeyouthere.fragments.UsersFragment

/**
 * Created by paulodichone on 7/29/17.
 */
class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when( position) {
             0 -> {
                 return  UsersFragment()
             }
            1 -> {
                return ChatsFragment()
            }
        }
        return null!!
    }

    override fun getCount(): Int {
       return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position) {
            0 -> return "USERS"
            1 -> return "CHATS"
        }
        return null!!
    }
}