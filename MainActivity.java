package com.coran.warsh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends Activity {
    private WebView web;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        web = new WebView(this);
        web.setBackgroundColor(0xFF0A1330);
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        web.addJavascriptInterface(new Object() {
            @JavascriptInterface public void share(String t) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, t);
                startActivity(Intent.createChooser(i, null));
            }
            @JavascriptInterface public String fonts() {
                try {
                    String[] f = getAssets().list("fonts");
                    StringBuilder sb = new StringBuilder("[");
                    boolean first = true;
                    for (String n : f) {
                        String l = n.toLowerCase();
                        if (l.endsWith(".ttf") || l.endsWith(".otf")) {
                            if (!first) sb.append(",");
                            sb.append("\"").append(n.replace("\\", "\\\\").replace("\"", "\\\"")).append("\"");
                            first = false;
                        }
                    }
                    return sb.append("]").toString();
                } catch (Exception e) { return "[]"; }
            }
            @JavascriptInterface public void fullscreen(final boolean on) {
                runOnUiThread(new Runnable() { public void run() { setFull(on); } });
            }
        }, "Android");
        web.loadUrl("file:///android_asset/index.html");
    }

    private void setFull(boolean on) {
        int f = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (on) f |= View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(f);
    }
}
