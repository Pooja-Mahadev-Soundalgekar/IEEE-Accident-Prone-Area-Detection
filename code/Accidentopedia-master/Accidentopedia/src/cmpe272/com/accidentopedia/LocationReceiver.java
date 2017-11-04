package cmpe272.com.accidentopedia;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {
	public LocationReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent i) {
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		Log.d("Broad", "here");
		Intent intent = new Intent(context, MapActivity.class);
		@SuppressWarnings("unchecked")
		ArrayList<LatLng> locs=	 (ArrayList<LatLng>) i.getSerializableExtra("Locations");
		intent.putExtra("points", locs);
	   
	}
}
