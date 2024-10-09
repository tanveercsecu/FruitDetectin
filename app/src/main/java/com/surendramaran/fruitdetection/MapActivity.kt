package com.surendramaran.fruitdetection

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val webView: WebView = findViewById(R.id.webview)

        // Enable JavaScript
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Make the WebView load URLs within the app
        webView.webViewClient = WebViewClient()

        // Load the HTML with the iframe
        val mapIframe = """
            <html>
            <body>
                <iframe 
                    width="100%" 
                    height="100%" 
                    frameborder="0" 
                    style="border:0" 
                    src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3153.8352546827015!2d-122.4216492846884!3d37.77492957975865!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x8085808c56f1e7bb%3A0xb47f5f3a747f18e9!2sGolden%20Gate%20Bridge!5e0!3m2!1sen!2sus!4v1616195791723!5m2!1sen!2sus"
                    allowfullscreen>
                </iframe>
            </body>
            </html>
        """.trimIndent()

        webView.loadData(mapIframe, "text/html", "UTF-8")
    }
}