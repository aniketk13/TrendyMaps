package com.example.trendymaps

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendymaps.databinding.ActivityMainBinding

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
                    showTweet(topic)
                }
            })
        }
        binding.recyclerview.adapter = adapter
    }

    private fun showTweet(topic: String) {
//        lifecycleScope.launch {
//            val url = "https://twitter.com/search?q=rest%20in%20peace"
////            traverse every topic and before making its url enter %20 after every word instead of space
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.setData(Uri.parse(url))
//            startActivity(intent)
//        }
    }
}