package cmpe272.com.accidentopedia;

import java.util.Locale;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Callingemergency extends Activity {

	TextToSpeech ttobj;
	double lat;
	double lng;
	int called=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callingemergency);
		Context context = getApplicationContext();

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// register PhoneStateListener
		PhoneStateListener callStateListener = new PhoneStateListener() {
			public void onCallStateChanged(int state, String incomingNumber) {
				// React to incoming call.
				// number=incomingNumber;
				// If phone ringing
				if (state == TelephonyManager.CALL_STATE_RINGING) {
					Toast.makeText(getApplicationContext(), "Phone Is Riging",
							Toast.LENGTH_LONG).show();

				}
				// If incoming call received
				if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
					Toast.makeText(getApplicationContext(),
							"Phone is Currently in A call", Toast.LENGTH_LONG)
							.show();
				}

				if (state == TelephonyManager.CALL_STATE_IDLE) {
					Toast.makeText(getApplicationContext(),
							"phone is neither ringing nor in a call",
							Toast.LENGTH_LONG).show();
				}
			}
		};
		telephonyManager.listen(callStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);

		// Location things
		LocationManager locationManager = (LocationManager) context
				.getSystemService(context.LOCATION_SERVICE);
		// final Location loc =
		// locationManager.getLastKnownLocation(LOCATION_SERVICE);

		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				lat = location.getLatitude();
				lng = location.getLongitude();
				Log.i("Latitude is are", Double.toString(lat));
				Log.i("Longitude is", Double.toString(lng));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				speakText();
				Log.i("Location", Double.toString(location.getLatitude()));

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
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Log.i("Location", "Provide Disabled");
			}

		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 0, locationListener);

		// Location things end here

		ttobj = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {
						if (status != TextToSpeech.ERROR) {
							ttobj.setLanguage(Locale.UK);
							Log.i("TexttoSpeect Enabled", "In Looop");
							speakText();
						}
					}
				});
	}

	@Override
	public void onPause() {
		if (ttobj != null) {
			ttobj.stop();
			ttobj.shutdown();
		}
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	public void speakText() {
		int f=0;
		while (f<5) {
			f++;
			try {
				Thread.sleep(11000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("Calling Emergency", "In Looop");
			if (!ttobj.isSpeaking()) {
				String toSpeak = "Calling Emergency 9 1 1, Coordinates are latitude "
						+ Double.toString(lat)
						+ " longitude "
						+ Double.toString(lng);
				Toast.makeText(getApplicationContext(), toSpeak,
						Toast.LENGTH_SHORT).show();

				ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
				if(called==0)
				{
					
					callEmergency();
					called =1;
				}

			}
		}
		

		
	}
	protected void callEmergency() {
		Log.i("MyActivity", "Caling emergency");
		Context cont = Callingemergency.this;
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		callIntent.setData(Uri.parse("tel:6509193890"));
		// context.Sta(callIntent);
		Log.i("Calling 911", "Caling Intent for calling");
		cont.startActivity(callIntent);
		// PendingIntent call1 = PendingIntent.getBroadcast(context,
		// 0,callIntent, 0);
		called = 1;

	}

}
