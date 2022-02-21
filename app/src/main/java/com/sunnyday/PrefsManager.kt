package com.sunnyday

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context?) {
    private val preferenceName = "sharedPreferences"
    private val mPrefs: SharedPreferences?
    private var mEditor: SharedPreferences.Editor? = null
    var localJson: String?
        get() = mPrefs!!.getString("response_json", null)
        set(json) {
            mEditor?.putString("response_json", json)
            mEditor?.commit()
        }

    init {
        mPrefs = context?.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        if (mPrefs != null) {
            mEditor = mPrefs.edit()
            mEditor?.apply()
        }
    }

    fun isFahrenheit(): Boolean {
        return mPrefs!!.getBoolean("fahrenheit", false)
    }

    fun setFahrenheit(value: Boolean) {
        mEditor!!.putBoolean("fahrenheit", value)
        mEditor!!.commit()
    }

}
