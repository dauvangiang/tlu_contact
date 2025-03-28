package com.mobile.group.tlucontact.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile.group.tlucontact.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        binding.tvAppName.text = "TLU Contact"
        binding.tvAppVersion.text = "Version 1.0.0"
        binding.tvDeveloperInfo.text = "Developed by Mobile Group"
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}