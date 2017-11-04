package cmpe272.com.accidentopedia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity{
	
	LocationManager locationManager;
	String provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
      
        Button startButton = (Button) findViewById(R.id.enable);
        startButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                	SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
                	 Log.d("Response", settings.getInt("id", -1)+"");
   				 if(settings.getInt("id", -1)==-1)
   				 {
   					 startActivity(new Intent(MainActivity.this,LoginActivity.class));
   				 }else{
                	startService(new  Intent(MainActivity.this,TrackService.class));
                     // check if GPS enabled     
   				 }
                      
                }
        });
        Button analytics = (Button) findViewById(R.id.analytics);
        analytics.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                	startActivity(new Intent(MainActivity.this,AnalyticsActivity.class)); // check if GPS enabled     
                    
                      
                }
        });
        Button report=(Button)findViewById(R.id.report);
        report.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,MainActivity1.class)); // check if GPS enabled     
                
			}
		});
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("id");
            editor.commit();
            finish();
        }
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    } 
   
}