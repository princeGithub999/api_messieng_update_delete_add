package com.example.notificationtest.DataModal

data class Notification(
    val android_channel_id: String= "notify_test",
    val body: String,
    val sound: Boolean,
    val title: String
)