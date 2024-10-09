package com.surendramaran.fruitdetection.youtube

import android.annotation.SuppressLint
import androidx.activity.enableEdgeToEdge
import com.surendramaran.fruitdetection.R
import com.surendramaran.fruitdetection.databinding.ActivityEmbeddedYoutubeMainBinding
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class EmbeddedYoutubeMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmbeddedYoutubeMainBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmbeddedYoutubeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupWebView()

        binding.buttonBack.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                Toast.makeText(this, "No history to go back", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonForward.setOnClickListener {
            if (binding.webView.canGoForward()) {
                binding.webView.goForward()
            } else {
                Toast.makeText(this, "No history to go forward", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonRefresh.setOnClickListener {
            binding.webView.reload()
        }

        binding.buttonLoadVideo.setOnClickListener {
            val newVideo = """
                <iframe width="100%" height="100%" src="https://www.youtube.com/embed/NEW_VIDEO_ID" 
                title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
            """
            binding.webView.loadData(newVideo, "text/html", "utf-8")
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        with(binding.webView) {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                    binding.progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.progressBar.visibility = View.GONE
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    Toast.makeText(this@EmbeddedYoutubeMainActivity, "Failed to load page", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
            webChromeClient = WebChromeClient()

            val video = """
                <iframe width="100%" height="100%" src="https://www.youtube.com/embed/QCX62YJCmGk?si=yBRYeQkAgfFzBwHq"
                title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
            """
            loadData(video, "text/html", "utf-8")
        }
    }
}
