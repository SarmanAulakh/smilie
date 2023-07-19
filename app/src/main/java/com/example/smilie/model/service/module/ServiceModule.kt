package com.example.smilie.model.service.module


import android.content.Context
import com.example.smilie.SmilieHiltApp
import com.example.smilie.model.service.UserService
import com.example.smilie.model.service.backend.AccountBackend
import com.example.smilie.model.service.backend.AccountBackendImpl
import com.example.smilie.model.service.backend.UserBackend
import com.example.smilie.model.service.backend.UserBackendImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    private const val BASE_URL = "http://10.0.2.2:5001"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): SmilieHiltApp {
        return app as SmilieHiltApp
    }

    @Singleton
    @Provides
    fun provideAccountBackend(auth: FirebaseAuth): AccountBackend {
        return AccountBackendImpl(auth)
    }

    @Singleton
    @Provides
    fun provideUserBackend(application: SmilieHiltApp, userService: UserService): UserBackend {
        return UserBackendImpl(application, userService)
    }
    @Singleton
    @Provides
    fun provideUserService(): UserService {
        return retrofit.create(UserService::class.java)
    }
}