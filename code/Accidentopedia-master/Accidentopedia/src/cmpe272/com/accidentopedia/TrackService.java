package cmpe272.com.accidentopedia;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.LangUtils;
import org.json.JSONArray;

import com.google.android.gms.maps.model.LatLng;

import android.provider.Settings;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class TrackService extends Service implements LocationListener {
	protected LocationManager locationManager;
    protected LocationListener locationListener;
    TextToSpeech ttobj;
    int ttobjinitialised=0;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled; 
	@Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	        Log.d("service", "Start service");
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	        Location loc = getLastBestLocation();
	        if(loc!=null)
	        getAccidentsFromServer(loc);
	    /*    Intent i = new Intent();
	        i.setAction("com.cmpe282.cloydsurvey.LocationBroadcast");
	        i.putExtra("lat", loc.getLatitude() + "");
	        i.putExtra("long", loc.getLongitude() + "");

	        sendBroadcast(i);*/
	        
	        ttobj = new TextToSpeech(TrackService.this,
					new TextToSpeech.OnInitListener() {
						@Override
						public void onInit(int status) {
							if (status != TextToSpeech.ERROR) {
								ttobj.setLanguage(Locale.UK);
								Log.i("TexttoSpeect Enabled", "In Looop");
								// speakText();
								ttobjinitialised = 1;
							}
						}
					});

	        
	        
	        return super.onStartCommand(intent, flags, startId);
	    }

	    @Override
	    public IBinder onBind(Intent intent) {
	        // TODO: Return the communication channel to the service.
	        return null;
	    }

	    @Override
	    public void onLocationChanged(android.location.Location location) {
	        latitude = location.getLatitude() + "";
	        longitude = location.getLongitude() + "";
	        getAccidentsFromServer(location);
	        Log.d("lat", latitude);
	   /*     Intent intent = new Intent();
	        intent.setAction("com.cmpe282.cloydsurvey.LocationBroadcast");
	        intent.putExtra("lat", latitude);
	        intent.putExtra("long", longitude);
	       
	        sendBroadcast(intent);*/
	    }

	    private Location getLastBestLocation() {
	        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

	        long GPSLocationTime = 0;
	        if (null != locationGPS) {
	            GPSLocationTime = locationGPS.getTime();
	        }

	        long NetLocationTime = 0;

	        if (null != locationNet) {
	            NetLocationTime = locationNet.getTime();
	        }

	        if (0 < GPSLocationTime - NetLocationTime) {
	            return locationGPS;
	        } else {
	            return locationNet;
	        }
	    }

	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {

	    }

	    @Override
	    public void onProviderEnabled(String provider) {

	    }

	    @Override
	    public void onProviderDisabled(String provider) {

	    }
    public void getAccidentsFromServer(final Location loc)
    {
    	Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpClient client = new DefaultHttpClient();
			HttpGet get=new HttpGet(AppConstants.URL1+"/accidents/"+loc.getLatitude()+"/"+loc.getLongitude());
			StringBuilder builder = new StringBuilder();
			try{
				HttpResponse response=client.execute(get);
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    builder.append(line);
                }
                ArrayList<LatLng> locs=new ArrayList<>();
                JSONArray jsonArr=new JSONArray(builder.toString());
               // Log.d("Array", jsonArr.toString());
                	for(int i=0;i<jsonArr.length();i++)
                	{
                		String point=jsonArr.getJSONObject(i).getString("location");
                		point=point.replace("POINT", "");
                		point=point.substring(1,point.length()-1);
                		LatLng ln=new LatLng(Double.parseDouble(point.split(" ")[0]), Double.parseDouble(point.split(" ")[1]));
                	//	Log.d(point, point.split(" ")[1]);
                		//latlong.add(point.split(" ")[0]);
                		//latlong.add(point.split(" ")[1]);
                		locs.add(ln);
                		//Log.d("response", point.substring(1, point.length()-1));
                	}
               
                broadCastResults(loc,locs);
			}
			catch(Exception e)
			{
				Log.d("response", e.toString());
			}
			}

			
		});
    	thread.start();
    }
    private void broadCastResults(Location loc, ArrayList<LatLng> locs) {
		// TODO Auto-generated method stub
    	//Log.d("here", "here");
    	Intent intent = new Intent(this, MapActivity.class);
    	int size=locs.size();
    	 locs=new ArrayList<LatLng>( locs.subList(0, 100));
    	 intent.putExtra("loc", loc);
		intent.putExtra("points", locs);
	    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

	    String strSpeak = "Approaching an area with "+size+" reported accidents. Please drive carefully. Click to view accident sites";
	    Notification noti = new NotificationCompat.Builder(this)
	        .setContentTitle("Approaching High Accident Prone area")
	        .setContentText("Approaching an area with "+size+" reported accidents. Please drive carefully. Click to view accident sites").setSmallIcon(R.drawable.logo)
	        .setContentIntent(pIntent)
	        .build();
	    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
	    // hide the notification after its selected
	    noti.flags |= Notification.FLAG_AUTO_CANCEL;
	    notificationManager.notify(0, noti);
	    // Speak here/
	    while(ttobjinitialised!=1)
	    {
	    	Log.d("loca", "While loop");
	    }
	    
	    ttobj.speak(strSpeak, TextToSpeech.QUEUE_FLUSH, null);
	    Intent in = new Intent();
	    in.putExtra("loc", loc);
	    Log.d("loca", locs.size()+"");
	    in.putExtra("points", locs);
	    in.setAction(".LocationReceiver");
	    sendBroadcast(in); 
	    //sendBroadcast(intent);
	}
    
}
