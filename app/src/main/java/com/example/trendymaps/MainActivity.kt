package com.example.trendymaps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendymaps.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var adapter: RecyclerView.Adapter<TopicsViewAdapter.ViewHolder>? = null //adapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager =
            LinearLayoutManager(this) //setting recycler view

        val topics = intent.getStringArrayListExtra("topics")
        Log.i("hellomain", topics.toString())

        showInRecyclerView(topics)
    }

    private fun showInRecyclerView(topics: ArrayList<String>?) {
        adapter = topics?.let {
            TopicsViewAdapter(it, object : TopicsViewAdapter.ItemClickListener {
                override fun onItemClick(topic: String, position: Int) {
                    Log.i("hello hello", topic)
                    showTweet(topic)
                }
            })
        }
        binding.recyclerview.adapter = adapter
    }

    private fun showTweet(topic: String) {
        if (topic[0] == '#') {
            var tempTopic = topic
            tempTopic = tempTopic.substring(1)
            lifecycleScope.launch {
                val url = "https://twitter.com/hashtag/$tempTopic"
                Log.i("url", url)
//            traverse every topic and before making its url enter %20 after every word instead of space
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        } else {
            lifecycleScope.launch {
                val tempUrl = formatUrl(topic)
                Log.i("url", tempUrl)
//            traverse every topic and before making its url enter %20 after every word instead of space
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(tempUrl)
                startActivity(intent)
            }
        }
    }

    private fun formatUrl(topic: String): String {
        var url: String = "https://twitter.com/search?q="
        for (char in topic) {
            if (char == ' ')
                url = "$url%20"
            else
                url += char
        }
        return url
    }
}