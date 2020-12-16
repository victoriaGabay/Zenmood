package com.example.zenmood.classes

import android.content.Context

class SharedPreferences(context: Context) {

    var preferences = context.getSharedPreferences("UserPreferences", 0)

     fun savePreferences(username: String, password: String, stayLogged: Boolean) {
        val editor = preferences.edit()
        editor.putString("username", username)//save username, password and if user wants to keep signed in
        editor.putString("password", password)
        editor.putBoolean("stayLogged", stayLogged)
        editor.apply()
    }

     fun checkStayLogged(): Boolean {
         return preferences.getBoolean("stayLogged", false)
    }
    fun changePreferences(pref : Boolean){
        val editor = preferences.edit()
        editor.remove("stayLogged")
        editor.putBoolean("stayLogged",pref)
        editor.apply()
    }
    fun getUserName() : String{
        return preferences.getString("username",null)!!
    }
    fun getPassword() : String{
        return preferences.getString("password", null)!!
    }






}