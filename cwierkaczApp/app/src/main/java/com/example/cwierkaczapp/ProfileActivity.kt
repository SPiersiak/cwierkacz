package com.example.cwierkaczapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.cwierkaczapp.util.DATA_USERS
import com.example.cwierkaczapp.util.DATA_USER_EMAIL
import com.example.cwierkaczapp.util.DATA_USER_USERNAME
import com.example.cwierkaczapp.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if(userId == null) {
            finish()
        }

        profileProgressLayout.setOnTouchListener { v, event -> true }

        populateInfo()
    }

    fun populateInfo() {
        profileProgressLayout.visibility = View.VISIBLE
        firebaseDB.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                usernameET.setText(user?.username, TextView.BufferType.EDITABLE)
                emailET.setText(user?.email, TextView.BufferType.EDITABLE)

                profileProgressLayout.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                finish()
            }
    }

    fun onApply(v:View){
        profileProgressLayout.visibility = View.VISIBLE
        val username = usernameET.text.toString()
        val email = emailET.text.toString()
        val map = HashMap<String, Any>()
        map[DATA_USER_USERNAME] = username
        map[DATA_USER_EMAIL] = email

        firebaseDB.collection(DATA_USERS).document(userId!!).update(map)
            .addOnSuccessListener {
                Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Update failed. Please try again.", Toast.LENGTH_SHORT).show()
                profileProgressLayout.visibility = View.GONE
            }
    }
    fun onSignout(v:View){
        firebaseAuth.signOut()
        finish()
    }
    companion object {
        fun newIntent(context: Context) = Intent(context, ProfileActivity::class.java)
    }
}