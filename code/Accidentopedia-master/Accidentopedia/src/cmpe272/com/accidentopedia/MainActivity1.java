package cmpe272.com.accidentopedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity1 extends Activity {
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	.permitAll().build();

static double lati;
static double lngi;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main1);
// callEmergency();
StrictMode.setThreadPolicy(policy);
final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
// Create an ArrayAdapter using the string array and a default spinner
// layout
ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		this, R.array.weather_array,
		android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
spinner.setAdapter(adapter);
LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
// get the last know location from your location manager.
final Location location = locationManager
		.getLastKnownLocation(LocationManager.GPS_PROVIDER);

LocationListener locationListener = new LocationListener() {
	public void onLocationChanged(Location location) {
		// Toast.makeText(context, "Current speed:" +
		// location.getSpeed(),
		// Toast.LENGTH_SHORT).show();

		lati = location.getLatitude();
		lngi = location.getLongitude();
		// speedChange(location.getSpeed());
	}

	@Override
	public void onStatusChanged(String provider, int status,
			Bundle extras) {
		Log.i("Location", "Status Changed");
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.i("Location", "Provider Enabled");
		// Log.i("Location", Double.toString(location.getLatitude()));

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.i("Location", "Provide Disabled");
	}

};
locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
		0, locationListener);

Button btnReport = (Button) findViewById(R.id.btnreport);
btnReport.setOnClickListener(new OnClickListener() {
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		Log.i("The Click", "Point 1");
		// instantiate the location manager, note you will need to
		// request permissions in your manifest
		// now get the lat/lon from the location and do something with
		// it.
		int spinnerpos = spinner.getSelectedItemPosition() + 1;
		Date d = new Date();
		int hours = d.getHours();
		int minutes = d.getMinutes();
		int day = d.getDate();
		int month = d.getMonth();
		int year = d.getYear();
		int week = 1;
		int weather = 0;
		//String vehicles = ((TextView) findViewById(R.id.vehicles)).getText()
				//.toString();
		//String persons = ((TextView) findViewById(R.id.persons))
				//.getText().toString();
		// String ddr = (TextView)findViewById(R.id.)
		// Starting reporting of the accident with rest api
		Log.i("The Click", "Point 2");
		HttpURLConnection httpcon = null;
		String url;
		String data;
		String result;
		Log.i("The Click", "Point 3");
		String lat = Double.toString(lati);
		Log.i("The Click", "Point 4");
		String lng = Double.toString(lngi);

		Geocoder gcd = new Geocoder(spinner.getContext(), Locale
				.getDefault());

		List<Address> addresses = null;
		try {
			addresses = gcd.getFromLocation(lati, lngi, 5);
			Log.i("The Click", "Point 5");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("The Click", "Error Point");
			e.printStackTrace();
		}
		Log.i("The Click", Integer.toString(addresses.size()));
		if (addresses.size() > 0) {
			Log.i("The Address", addresses.get(0).getLocality());
		}
		// System.out.println(addresses.get(0).getLocality());

		execute(lat, lng, "-1", "-1", "-1", Integer.toString(month),
				Integer.toString(spinnerpos), Integer.toString(hours),
				"-1", Integer.toString(day), Integer.toString(week),
				Integer.toString(year), Integer.toString(minutes), "-1");
		/*
		 * public static void execute(String lat, String lng, String c,
		 * String d,String f, String m, String w, String h, String s,
		 * String day, String week, String y, String mint,String county)
		 * 
		 * 
		 * 
		 * 
		 * try { httpcon = (HttpURLConnection) ((new URL(
		 * "http://10.0.0.15:3412/report").openConnection()));
		 * Log.i("The Click", "Point 3"); httpcon.setDoOutput(true);
		 * Log.i("The Click", "Point 3.1");
		 * httpcon.setRequestProperty("Content-Type",
		 * "application/json"); Log.i("The Click", "Point 3.2");
		 * httpcon.setRequestProperty("Accept", "application/json");
		 * Log.i("The Click", "Point 3.3"); httpcon.connect();
		 * Log.i("The Click", "Point 4"); OutputStream os;
		 * Log.i("The Click", "Point 5"); os =
		 * httpcon.getOutputStream(); Log.i("The Click", "Point 6");
		 * BufferedWriter writer = new BufferedWriter( new
		 * OutputStreamWriter(os, "UTF-8")); writer.write(
		 * "{\"lat\":\"2\",\"long\":\"73\",\"city\":\"350\",\"ddr\":\"1\",\"fatal\":\"73\",\"month\":\"350\",\"weather\":\"1\",\"hour\":\"73\",\"state\":\"350\",\"day\":\"1\",\"week\":\"73\",\"year\":\"350\",\"minute\":\"350\",\"county\":\"350\"}"
		 * ); writer.close(); os.close(); Log.i("The Click", "Point 7");
		 * BufferedReader br; Log.i("The Click", "Point 8"); br = new
		 * BufferedReader(new InputStreamReader(httpcon
		 * .getInputStream(), "UTF-8")); Log.i("The Click", "Point 9");
		 * String line = null; StringBuilder sb = new StringBuilder();
		 * 
		 * while ((line = br.readLine()) != null) { sb.append(line); }
		 * 
		 * br.close(); result = sb.toString(); Log.i("The result",
		 * result);
		 * 
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}
});

}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
getMenuInflater().inflate(R.menu.main, menu);
return true;
}

public static void execute(String lat, String lng, String c, String d,
	String f, String m, String w, String h, String s, String day,
	String week, String y, String mint, String county) {
Map<String, String> comment = new HashMap<String, String>();
comment.put("lat", lat);
comment.put("long", lng);
comment.put("city", c);
comment.put("ddr", d);
comment.put("fatal", f);
comment.put("month", m);
comment.put("weather", w);
comment.put("hour", h);
comment.put("state", s);
comment.put("day", day);
comment.put("week", week);
comment.put("year", y);
comment.put("minute", mint);
comment.put("county", county);
Log.i("The Click", "Point 1");
String json = new GsonBuilder().create().toJson(comment, Map.class);
Log.i("The Click", "Point 2");
Log.i("The Click", json);
HttpResponse res = makeRequest(AppConstants.URL1+"/report", json);
Log.i("The Click", "Point 3");
HttpEntity entity = res.getEntity();
String result = "Error";
try {
	result = EntityUtils.toString(entity);
} catch (ParseException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
;
Log.i("The Click", result);
}

public static HttpResponse makeRequest(String uri, String json) {
try {
	HttpPost httpPost = new HttpPost(uri);
	httpPost.setEntity(new StringEntity(json));
	// httpPost.setHeader("Accept", "application/json");
	// httpPost.setHeader("Content-type", "application/json");
	return new DefaultHttpClient().execute(httpPost);
} catch (UnsupportedEncodingException e) {
	e.printStackTrace();
} catch (ClientProtocolException e) {
	e.printStackTrace();
} catch (IOException e) {
	e.printStackTrace();
}
return null;
}


protected void callEmergency() {
Intent callIntent = new Intent(Intent.ACTION_CALL);
callIntent.setData(Uri.parse("tel:6509193890"));
startActivity(callIntent);
}
}
