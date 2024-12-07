package com.anikinkirill.tapyou.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anikinkirill.tapyou.R
import com.anikinkirill.tapyou.databinding.ActivityMainBinding
import com.anikinkirill.tapyou.presentation.config.TapYouPointsConfigFragment

class TapYouMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigateToConfigFragment()
    }

    private fun navigateToConfigFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainerFrameLayout, TapYouPointsConfigFragment())
            .commit()
    }
}