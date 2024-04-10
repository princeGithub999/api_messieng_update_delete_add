package com.example.notificationtest.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.notificationtest.R
import com.google.firebase.auth.FirebaseAuth

class SplaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splace)

        val auth = FirebaseAuth.getInstance()

        Handler().postDelayed({
             startActivity(Intent(this,MainActivity::class.java))
            finish()
        }, 3000)


    }
}