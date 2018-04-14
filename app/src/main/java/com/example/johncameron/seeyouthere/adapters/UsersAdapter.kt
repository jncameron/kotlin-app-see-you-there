package com.example.johncameron.seeyouthere.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.johncameron.seeyouthere.R
import com.example.johncameron.seeyouthere.activities.ChatActivity
import com.example.johncameron.seeyouthere.activities.MainActivity
import com.example.johncameron.seeyouthere.activities.ProfileActivity
import com.example.johncameron.seeyouthere.models.Users
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.logging.Logger

class UsersAdapter(databaseQuery: DatabaseReference, var context: Context)
    :FirebaseRecyclerAdapter<Users, UsersAdapter.ViewHolder>(
        Users::class.java,
        R.layout.users_row,
        UsersAdapter.ViewHolder::class.java,
        databaseQuery

){
    override fun getItemCount(): Int {
        return super.getItemCount()
    }


    val Log = Logger.getLogger(MainActivity::class.java.name)
    override fun populateViewHolder(viewHolder: UsersAdapter.ViewHolder?, user: Users?, position: Int) {
        var userId = getRef(position).key // the unique firebase keyid for this current user!
        viewHolder!!.bindView(user!!, context)

        viewHolder.itemView.setOnClickListener {
            //Create an AlertDialog to prompt users if they want to see profile or send message



            var options = arrayOf("Open Profile", "Send Message")
            var builder = AlertDialog.Builder(context)
            builder.setTitle("Select Options")
            builder.setItems(options, DialogInterface.OnClickListener { dialogInterface, i ->
                var userName = viewHolder.userNameTxt
                var userStat = viewHolder.userStatusTxt
                var profilePic = viewHolder.userProfilePicLink

                if (i == 0) {
                    //open user profile

                    var profileIntent = Intent(context, ProfileActivity::class.java)
                    profileIntent.putExtra("userId", userId)
                    context.startActivity(profileIntent)

                } else {
                    //Send Message/ ChatActivity
                    var chatIntent = Intent(context, ChatActivity::class.java)
                    chatIntent.putExtra("userId", userId)
                    chatIntent.putExtra("name", userName)
                    chatIntent.putExtra("status", userStat)
                    chatIntent.putExtra("profile", profilePic)
                    context.startActivity(chatIntent)
                }

            })

            builder.show()

        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var userNameTxt: String? = null
        var userStatusTxt: String? = null
        var userProfilePicLink: String? = null


        fun bindView(user: Users, context: Context) {
            var userName = itemView.findViewById<TextView>(R.id.userName)
            var userStatus = itemView.findViewById<TextView>(R.id.userStatus)
            var userProfilePic = itemView.findViewById<CircleImageView>(R.id.usersProfile)

            //set the strings so we can pass in the intent
            userNameTxt = user.display_name
            userStatusTxt = user.user_status
            userProfilePicLink = user.thumb_image

            userName.text = user.display_name
            userStatus.text = user.user_status

            Picasso.with(context)
                    .load(userProfilePicLink)
                    .placeholder(R.drawable.profile_img)
                    .into(userProfilePic)

        }

    }

}