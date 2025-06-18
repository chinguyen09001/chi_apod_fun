package com.example.chiapodfun.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.chiapodfun.R
import com.example.chiapodfun.ui.utils.ApodFragment

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ApodFragment())
            .commit()
    }

}