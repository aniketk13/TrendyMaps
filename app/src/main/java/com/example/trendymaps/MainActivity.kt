package com.example.trendymaps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val topics = intent.getStringArrayListExtra("topics")
        Log.i("hellomain", topics.toString())
        lifecycleScope.launch {
            val url = "https://twitter.com/search?q=rest%20in%20peace"
//            traverse every topic and before making its url enter %20 after every word instead of space
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }
    }
}