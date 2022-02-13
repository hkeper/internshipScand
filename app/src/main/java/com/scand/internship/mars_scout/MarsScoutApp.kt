package com.scand.internship.mars_scout

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.scand.internship.mars_scout.di.AppComponent
import com.scand.internship.mars_scout.di.MapsModule
import com.scand.internship.mars_scout.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.realm.Realm
import javax.inject.Inject

class MarsScoutApp : MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .mapsModule(MapsModule())
            .build().run {
                inject(this@MarsScoutApp)
            }
        Realm.init(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}

