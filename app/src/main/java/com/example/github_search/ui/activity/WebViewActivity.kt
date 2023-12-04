package com.example.github_search.ui.activity

import android.os.Bundle
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.example.github_search.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private lateinit var url:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url= intent.getStringExtra("url")?:""
        if(url.isNotEmpty()){

            val webSettings: WebSettings = binding.webview.settings
            webSettings.javaScriptEnabled = true

            binding.webview.loadUrl(url)

        }
    }
}