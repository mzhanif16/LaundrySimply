package com.example.laundrysimply

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.multidex.MultiDexApplication
import com.example.laundrysimply.network.HttpClient
import com.onesignal.OneSignal

// Replace the below with your own ONESIGNAL_APP_ID
const val ONESIGNAL_APP_ID = "8d8f6cfd-3268-471a-956a-353c92454074"
class LaundrySimply : MultiDexApplication() {

    companion object{
        lateinit var instance : LaundrySimply

        fun getApp() : LaundrySimply{
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
        instance = this
    }

    fun getPreferences() : SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(this)
    }

    fun setToken(token:String){
        getPreferences().edit().putString("PREFERENCES_TOKEN",token).apply()
        HttpClient.getInstance().buildRetrofitClient(token)
    }

    fun getToken():String?{
        return getPreferences().getString("PREFERENCES_TOKEN",null)
    }

    fun setUser(user:String){
        getPreferences().edit().putString("PREFERENCES_USER",user).apply()
        HttpClient.getInstance().buildRetrofitClient(user)
    }

    fun getUser():String?{
        return getPreferences().getString("PREFERENCES_USER",null)
    }

    fun removeToken() {
        getPreferences().edit().remove("PREFERENCES_TOKEN").apply()
    }
}