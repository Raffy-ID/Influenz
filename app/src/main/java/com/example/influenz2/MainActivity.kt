package com.example.influenz2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Langsung alihkan ke SplashActivity sebagai layar awal
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }
}
