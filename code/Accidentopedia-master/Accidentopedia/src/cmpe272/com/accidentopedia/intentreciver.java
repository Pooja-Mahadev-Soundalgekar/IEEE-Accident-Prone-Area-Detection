package cmpe272.com.accidentopedia;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class intentreciver extends BroadcastReceiver {
	protected int callEmergency( Context context) {
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:6509193890"));
		PendingIntent call1 = PendingIntent.getBroadcast(context, 0,callIntent, 0);
		return 0;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("MyActivity", "Its coming for receive 1");
		
		
			callEmergency(context);
		
		
		
	}
}
