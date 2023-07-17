package com.example.smilie.screens

import android.app.Application
import android.widget.Toast
import com.example.smilie.model.User
import com.example.smilie.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import com.example.smilie.api_interfaces.UserApi
import android.util.Log

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val accountService: AccountService
    ) : SmilieViewModel() {

    var userData: User? = null

    init {
        getUserDetails()
    }

    fun getUserDetails(userId: String = accountService.currentUserId) {
        if (userId != "") {
            Log.d("BACKEND", "Preparing to call api server 1")
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5001")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(UserApi::class.java)
            val call: Call<User?>? = api.getUserById(userId)
            Log.d("BACKEND", "Calling api server")

            call!!.enqueue(object: Callback<User?> {
                override fun onResponse(call: Call<User?>, response: Response<User?>) {
                    if(response.isSuccessful) {
                        userData = response.body()
                    } else {
                        Toast.makeText(
                            application,
                            "user data not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.d("TAG", "failed " + t.message)
                    Toast.makeText(
                        application,
                        "Failed to get user data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}