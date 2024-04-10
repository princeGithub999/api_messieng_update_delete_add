package com.example.notificationtest.Activity

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.notificationtest.DataModal.Data
import com.example.notificationtest.DataModal.DeleteNotificationDataModal
import com.example.notificationtest.DataModal.Notification
import com.example.notificationtest.DataModal.UserDataModel
import com.example.notificationtest.NotificationApi
import com.example.notificationtest.databinding.ActivityRijestireBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.UUID

class AddDataActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRijestireBinding
    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRijestireBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                Log.d("Tocken",it.toString())
            }
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),10)
        }

        binding.registerButton.setOnClickListener {
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                registerData(it)
            }

        }

    }

    private fun registerData(token: String) {

        val name = binding.userNameEditeText.text.toString()
        val email = binding.userEmailEditeText.text.toString()

        val uid = UUID.randomUUID().toString()

        val map = UserDataModel(name,email,uid,token)

        db.collection("Register").document(uid).set(map)
            .addOnSuccessListener {
                Toast.makeText(this, "Data add success", Toast.LENGTH_SHORT).show()
                sendNotification(name,token)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data add fell", Toast.LENGTH_SHORT).show()
            }

    }

    @SuppressLint("CheckResult")
    fun sendNotification(name: String,token: String) {

        val addDataNotification = DeleteNotificationDataModal(
            Data("23", "mnnk"),
            Notification("notify_test", "${name} is added", true, "add"),
            arrayListOf(token)
        )

        val headerMap = HashMap<String, String>()
        headerMap["Authorization"] = "key=AAAA_g3NChA:APA91bF4YZlEg7ls0QZI0ZBetqKN2qDCFYjxYgFOB8r5iZY50tftfX78CGHuewfo5aebZbvTbqfhct03xgrgJBB6tc4Qw4ulSYOn_AfI-P8iOckc1g-GvX7om6I9sQavkMAFqu9WL6VD"

        NotificationApi.createRetroFit().deleteNotification(headerMap,addDataNotification)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show()
                    finish()
                },
                { error ->
                    // Handle error
                    Log.e("RxJava", "An error occurred: ${error.message}", error)
                    Toast.makeText(this, "An error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
    }
}