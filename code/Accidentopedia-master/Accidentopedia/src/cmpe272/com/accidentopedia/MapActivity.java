package cmpe272.com.accidentopedia;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;


public class MapActivity extends Activity {
	GoogleMap mMap;
	Location loc=null;
	LocChangeReceiver locr=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		Log.d("Points", getIntent().getSerializableExtra("points")+"");
		ArrayList<LatLng> points= (ArrayList<LatLng>) getIntent().getSerializableExtra("points");
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		Log.d("Clear", points.size()+"");
		if(points.size()>0)
	{
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(points.get(0), 11.0f);
		mMap.animateCamera(yourLocation);
		for(int i=0;i<points.size()&&i<100;i++)
		{
			mMap.addMarker(new MarkerOptions()
	        .position(points.get(i)));
		}	}
		
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	public class LocChangeReceiver
	
	      extends BroadcastReceiver {
	
	 
	
	   @Override
	
	   public void onReceive
	
	         (Context context, Intent intent) {
	
	     
	
	   }
	
	 
	
	}
}
