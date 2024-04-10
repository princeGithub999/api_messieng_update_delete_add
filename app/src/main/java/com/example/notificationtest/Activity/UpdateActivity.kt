package com.example.notificationtest.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.notificationtest.DataModal.Data
import com.example.notificationtest.DataModal.DeleteNotificationDataModal
import com.example.notificationtest.DataModal.Notification
import com.example.notificationtest.NotificationApi
import com.example.notificationtest.databinding.ActivityUpdateBinding
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UpdateActivity : AppCompatActivity() {


    private lateinit var binding: ActivityUpdateBinding
    private var uid: String = ""
    private var tokenId: String = ""
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        uid = intent.getStringExtra("uid").toString()
        tokenId = intent.getStringExtra("TokenId").toString()

        binding.userNameUpdateEditeText.setText(name)
        binding.userEmailUpdateEditeText.setText(email)

        binding.updateButton.setOnClickListener {
            updateData()
        }

    }

    private fun updateData() {
        val updateName = binding.userNameUpdateEditeText.text.toString()
        val updateEmail = binding.userEmailUpdateEditeText.text.toString()

        val updateData = hashMapOf(
            "name" to updateName,
            "email" to updateEmail,
            "uid" to uid
        )

        db.collection("Register").document(uid).update(updateData as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Update data success", Toast.LENGTH_SHORT).show()
                finish()
                sendUpdateMessage()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update fell", Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("CheckResult")
    fun sendUpdateMessage() {
        val name = binding.userNameUpdateEditeText.text.toString()
        val valupdateDataNotification = DeleteNotificationDataModal(
            Data("23", "mnnk"), Notification("notify_test", "${name}", true, "Update"),
            arrayListOf("${tokenId}")
        )

        val headerMap = HashMap<String, String>()
        headerMap["Authorization"] =
            "key=AAAA_g3NChA:APA91bF4YZlEg7ls0QZI0ZBetqKN2qDCFYjxYgFOB8r5iZY50tftfX78CGHuewfo5aebZbvTbqfhct03xgrgJBB6tc4Qw4ulSYOn_AfI-P8iOckc1g-GvX7om6I9sQavkMAFqu9WL6VD"
        NotificationApi.createRetroFit().deleteNotification(headerMap, valupdateDataNotification)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({result->
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            },
                { error ->
                    // Handle error
                    Log.e("RxJava", "An error occurred: ${error.message}", error)
                    Toast.makeText(this, "An error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
                })
                
            
    }
}


