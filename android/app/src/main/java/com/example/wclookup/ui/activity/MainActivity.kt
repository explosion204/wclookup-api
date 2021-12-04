package com.example.wclookup.ui.activity

import android.os.Bundle
import com.example.wclookup.R
import com.example.wclookup.ui.fragment.AuthFragment
import com.example.wclookup.ui.viewmodel.factory.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

//    private val testViewModel: TestViewModel by viewModels {
//        viewModelFactory
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, AuthFragment())
            .commit()
    }
}