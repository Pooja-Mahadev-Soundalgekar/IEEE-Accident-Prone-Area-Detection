package cmpe272.com.accidentopedia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ChartActivity extends Activity {

	WebView web;
    ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		Intent intent = getIntent();
        String chartType = intent.getStringExtra("URL");
        web = (WebView) findViewById(R.id.web);
       web.getSettings().setSupportZoom(true);
        web.getSettings().setJavaScriptEnabled(true);
        Log.d("url",AppConstants.URL1+chartType);
        web.loadUrl(AppConstants.URL1+chartType);
	}
	
}
