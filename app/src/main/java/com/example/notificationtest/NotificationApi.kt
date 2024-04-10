package com.example.notificationtest



import com.example.notificationtest.DataModal.DeleteNotificationDataModal
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.HeaderMap


interface NotificationApi {

    @POST("send")
    fun deleteNotification(@HeaderMap headers: HashMap<String,String>, @Body post: DeleteNotificationDataModal): Observable<Any>




    companion object Factory {
        fun createRetroFit(): NotificationApi {
            val retroFit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .build()
                .create(NotificationApi::class.java)
            return retroFit
        }
    }
}