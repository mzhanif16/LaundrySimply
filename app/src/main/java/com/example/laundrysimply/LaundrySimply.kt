package com.example.laundrysimply

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.multidex.MultiDexApplication
import com.example.laundrysimply.network.HttpClient

class LaundrySimply : MultiDexApplication() {
    companion object{
        lateinit var instance : LaundrySimply

        fun getApp() : LaundrySimply{
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
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