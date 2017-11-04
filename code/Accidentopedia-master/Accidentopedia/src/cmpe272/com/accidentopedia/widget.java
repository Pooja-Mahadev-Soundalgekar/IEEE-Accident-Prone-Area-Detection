package cmpe272.com.accidentopedia;

import java.util.Locale;
import java.util.Timer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class widget extends AppWidgetProvider {
	Context cont;
	TextToSpeech ttobj;
	float[] speedArray = new float[50];;
	ComponentName thisWidget;
	protected void speedChange(float oSpeed) {
		for (int i = 0; i < 49; i++) {
			speedArray[i + 1] = speedArray[i];
		}
		speedArray[0] = oSpeed;
		if (speedArray[4] - speedArray[0] == 0.0f) {
			RemoteViews views = new RemoteViews(cont.getPackageName(),
					R.layout.widget);
			
			//callEmergency();
			//speakText(views);
		}
	}

	@Override
	public void onUpdate(Context context, final AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.i("MyActivity", Integer.toString(appWidgetIds.length));
		Log.i("MyActivity", "onUpdate(): ");
		
		for (int appWidgetId : appWidgetIds) {

			//Intent callIntent = new Intent( Intent.ACTION_CALL);
			//callIntent.setData(Uri.parse("tel:6509193890"));
			//PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					//callIntent, 0);

			Intent callIntent = new Intent(Intent.ACTION_MAIN, null);
			callIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			callIntent.putExtra("testExtra",true);
			final ComponentName cn = new ComponentName(context, Callingemergency.class);
			callIntent.setComponent(cn);
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//callIntent.setClass(context.getPackageName(), callingemergency.class)			
			//callIntent.setClassName(".callemergency", ".callemergency");
			//callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//PendingIntent pendingIntent = PendingIntent.getService(context, 0, callIntent, Intent.FLAG_ACTIVITY_NO_ANIMATION); 
			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, callIntent, Intent.FLAG_ACTIVITY_NO_ANIMATION); 
			
			
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget);
			
			views.setOnClickPendingIntent(R.id.button1, pendingIntent);

			// Tell the AppWidgetManager to perform an update on the current App
			// Widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
			
			final Context c ;
			c= context;
			cont = context;
			LocationManager locationManager = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);
			LocationListener locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					location.getLatitude();
					//Toast.makeText(context, "Current speed:" + location.getSpeed(),
							//Toast.LENGTH_SHORT).show();
					
					
					Log.i("Location", Double.toString(location.getLatitude()));
					RemoteViews views = new RemoteViews(c.getPackageName(),
							R.layout.widget);

					views.setTextViewText(R.id.textView1,
							"Current location =  " + Double.toString(location.getLatitude()) +","+ Double.toString(location.getLongitude()));
					views.setTextViewText(R.id.textView2,
							"Current Speed =  " +  location.getSpeed());

					
					thisWidget = new ComponentName(c, widget.class);
					appWidgetManager.updateAppWidget(thisWidget, views);
					
					speedChange(location.getSpeed());
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
					//Log.i("Location", Double.toString(location.getLatitude()));
					RemoteViews views = new RemoteViews(c.getPackageName(),
							R.layout.widget);

					views.setTextViewText(R.id.textView1,
							"Current location =  ");
					
					thisWidget = new ComponentName(c, widget.class);
					appWidgetManager.updateAppWidget(thisWidget, views);

				}

				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					Log.i("Location", "Provide Disabled");		
				}

			
				};
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
						0, locationListener);

			
			
			

			Timer timer = new Timer();
			if (!timertick.isRunning) {
				timer.scheduleAtFixedRate(new timertick(context,
						appWidgetManager), 1, 1000);
			}
		}
		
		
		/*ttobj=new TextToSpeech(cont, 
			      new TextToSpeech.OnInitListener() {
			      public void onInit(int status) {
			         if(status != TextToSpeech.ERROR){
			             ttobj.setLanguage(Locale.UK);
			            }				
			         }
			      });

					   
			   
        /*
		 * public void onUpdate(Context context, AppWidgetManager
		 * appWidgetManager, int[] appWidgetIds) { Log.i("MyActivity",
		 * "Its coming for receive 9"); //final int N = appWidgetIds.length;
		 * 
		 * // Perform this loop procedure for each App Widget that belongs to
		 * this // provider Log.i("MyActivity", "Its coming for receive 2"); //
		 * Create an Intent to launch ExampleActivity Intent intent = new
		 * Intent(context, MainActivity.class); PendingIntent pendingIntent =
		 * PendingIntent.getActivity(context, 0, intent, 0);
		 * 
		 * // Get the layout for the App Widget and attach an on-click listener
		 * // to the button RemoteViews remoteViews = new
		 * RemoteViews(context.getPackageName(), R.layout.widget);
		 * remoteViews.setOnClickPendingIntent(R.id.button1,
		 * buildButtonPendingIntent(context));
		 * 
		 * pushWidgetUpdate(context, remoteViews);
		 * remoteViews.setOnClickPendingIntent(R.id.button1, pendingIntent); }
		 * 
		 * public static PendingIntent buildButtonPendingIntent(Context context)
		 * { Intent intent = new Intent(); //
		 * intent.setAction("intent.action.CALL_EMERGENCY"); Log.i("MyActivity",
		 * "Its coming for receive 3"); return
		 * PendingIntent.getBroadcast(context, 0, intent,
		 * PendingIntent.FLAG_UPDATE_CURRENT); }
		 * 
		 * public static void pushWidgetUpdate(Context context, RemoteViews
		 * remoteViews) { ComponentName myWidget = new ComponentName(context,
		 * widget.class); Log.i("MyActivity", "Its coming for receive 4");
		 * AppWidgetManager manager = AppWidgetManager.getInstance(context);
		 * manager.updateAppWidget(myWidget, remoteViews); }
		 */
	}

	protected void callEmergency() {
		Log.i("MyActivity", "Caling emergency");
		Context context = cont;
		// Intent callIntent = new Intent(context, intentreciver.class);
		// context.startService(callIntent);
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		callIntent.setData(Uri.parse("tel:6509193890"));
		// context.Sta(callIntent);
		Log.i("MyActivity", "Caling Intent");
		cont.startActivity(callIntent);
		// PendingIntent call1 = PendingIntent.getBroadcast(context,
		// 0,callIntent, 0);

	}
	

}
