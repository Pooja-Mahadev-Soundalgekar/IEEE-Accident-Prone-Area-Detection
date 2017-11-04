package cmpe272.com.accidentopedia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

class timertick extends TimerTask {
	static int count = 0;
	public static boolean isRunning = false;
	float[] speedArray = new float[20];;

	protected void speedChange(float oSpeed) {
		for (int i = 0; i < 49; i++) {
			speedArray[i + 1] = speedArray[i];
		}
		speedArray[0] = oSpeed;
		if (speedArray[4] - speedArray[0] > 20.0f) {
			callEmergency();
		}
	}

	Context cont;
	Button b1;
	RemoteViews remoteViews;
	AppWidgetManager appWidgetManager;
	ComponentName thisWidget;
	DateFormat format = SimpleDateFormat.getTimeInstance(
			SimpleDateFormat.MEDIUM, Locale.getDefault());

	public timertick(Context context, AppWidgetManager appWidgetManager) {
		cont = context;
		this.appWidgetManager = appWidgetManager;
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

		thisWidget = new ComponentName(context, widget.class);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		this.isRunning = true;
		Date dt = new Date();
		this.count++;
		// remoteViews.setTextViewText(R.id.textView1,
		// "Time = " + format.format(dt));

		//Log.i("MyActivity", Integer.toString(this.count));

		if (dt.getSeconds() % 30 == 0) {
			Log.i("MyActivity", "Count 10");
			// callEmergency();
		}

		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
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
