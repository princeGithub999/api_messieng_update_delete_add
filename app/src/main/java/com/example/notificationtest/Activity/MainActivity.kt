package com.example.notificationtest.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notificationtest.Adapter.UserAdapter
import com.example.notificationtest.DataModal.Data
import com.example.notificationtest.DataModal.UserDataModel
import com.example.notificationtest.DataModal.DeleteNotificationDataModal
import com.example.notificationtest.DataModal.Notification
import com.example.notificationtest.NotificationApi
import com.example.notificationtest.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(),UserAdapter.DeleteApi {

    private lateinit var binding: ActivityMainBinding
    val db  = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            val token = it
            println(it)
        }
        getData()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.addFlotingButton.setOnClickListener {
            startActivity(Intent(this,AddDataActivity::class.java))
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getData(){
        db.collection("Register").get()
            .addOnSuccessListener {document->

                val list = ArrayList<UserDataModel>()
                for (a in document){
                    val data = a.toObject(UserDataModel::class.java)
                        list.add(data)
                    Toast.makeText(this, "get Data success", Toast.LENGTH_SHORT).show()
                }

                val adapter = UserAdapter(this,list,this)
                binding.userListRecycleView.layoutManager = LinearLayoutManager(this)
                binding.userListRecycleView.adapter = adapter
            }.addOnFailureListener {
                Toast.makeText(this, "get Data feel", Toast.LENGTH_SHORT).show()
            }
    }


    override fun deleteNotification(list: UserDataModel) {
        sendNotification(list)
    }
    @SuppressLint("CheckResult")
    fun sendNotification(user: UserDataModel) {
        val postData = DeleteNotificationDataModal(
            Data("23", "mnnk"),
            Notification("notify_test", "${user.name} is deleted", true, "Deleted"),
            arrayListOf(user.tockenId.toString())
        )
        val headerMap = HashMap<String, String>()
        headerMap["Authorization"] = "key=AAAA_g3NChA:APA91bF4YZlEg7ls0QZI0ZBetqKN2qDCFYjxYgFOB8r5iZY50tftfX78CGHuewfo5aebZbvTbqfhct03xgrgJBB6tc4Qw4ulSYOn_AfI-P8iOckc1g-GvX7om6I9sQavkMAFqu9WL6VD"
        NotificationApi.createRetroFit().deleteNotification(headerMap,postData)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    // Handle error
                    Log.e("RxJava", "An error occurred: ${error.message}", error)
                    Toast.makeText(this, "An error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}