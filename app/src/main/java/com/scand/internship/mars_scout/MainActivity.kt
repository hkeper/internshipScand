package com.scand.internship.mars_scout

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import timber.log.Timber

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        Timber.plant(Timber.DebugTree())
    }

}
