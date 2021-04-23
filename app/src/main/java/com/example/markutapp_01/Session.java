package com.example.markutapp_01;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String userEmail) {
        prefs.edit().putString("UserEmail", userEmail).commit();
    }

    public String getusename() {
        String usename = prefs.getString("UserEmail","");
        return usename;
    }
    public void setSecUserEmail(String secEmail) {
        prefs.edit().putString("SecurityUserEmail", secEmail).commit();
    }

    public String getSecUserEmail() {
        String usename = prefs.getString("SecurityUserEmail","");
        return usename;
    }
}
