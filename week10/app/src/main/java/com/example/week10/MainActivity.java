package com.example.week10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    EditText inputAddress;
    String currentUrl = "";
    ArrayList<String> historyPrev = new ArrayList<String>();
    ArrayList<String> historyNext = new ArrayList<String>();
    int historySize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        inputAddress = (EditText) findViewById(R.id.input_address);
        inputAddress.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    loadURL(inputAddress.getText().toString(), true);
                    return true;
                }
                return false;
            }
        });
    }

    void loadURL(String url, boolean addToHistory) {
        String finalUrl = url;
        if (url.equals("index.html")) {
            finalUrl = "file:///android_asset/index.html";
        } else {
            if (!url.startsWith("http://")) {
                finalUrl = "http://" + url;
            }
        }

        // Clear next history
        if (!currentUrl.isEmpty() && addToHistory) {
            historyNext.clear();
            fixedAdd(historyPrev, currentUrl, historySize);
        }

        webView.loadUrl(finalUrl);
        currentUrl = url;
        inputAddress.setText(url);
    }

    <T> void fixedAdd(ArrayList<T> list, T val, int size) {
        list.add(val);
        if (list.size() > size) list.remove(0);
    }

    public void refreshPage(View view) {
        loadURL(currentUrl, false);
    }

    public void nextPage(View view) {
        if (!historyNext.isEmpty()) {
            String item = historyNext.get(historyNext.size() - 1);
            historyNext.remove(historyNext.size() - 1);
            fixedAdd(historyPrev, currentUrl, historySize);
            loadURL(item, false);
        }
    }

    public void previousPage(View view) {
        if (!historyPrev.isEmpty()) {
            String item = historyPrev.get(historyPrev.size() - 1);
            historyPrev.remove(historyPrev.size() - 1);
            fixedAdd(historyNext, currentUrl, historySize);
            loadURL(item, false);
        }
    }

    public void initializeClick(View view) {
        if (currentUrl.equals("index.html"))
            webView.evaluateJavascript("javascript:initialize()", null);
    }

    public void shoutoutClick(View view) {
        if (currentUrl.equals("index.html"))
            webView.evaluateJavascript("javascript:shoutOut()", null);
    }
}
