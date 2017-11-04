package cmpe272.com.accidentopedia;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class SettingsActivity extends Activity {
	RadioButton fifty,seventyFIve,hundres,yes,no;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		 SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
	       int allowDrunk = settings.getInt("drunk", -1);
	       int numMarkers=settings.getInt("numMarkers", 50);
		fifty=(RadioButton)findViewById(R.id.fifty);
		seventyFIve=(RadioButton)findViewById(R.id.seventyFive);
		hundres=(RadioButton)findViewById(R.id.hundred);
		 yes=(RadioButton)findViewById(R.id.yes);
		 no=(RadioButton)findViewById(R.id.no);
		 if(numMarkers==50)
		 {
			 fifty.setChecked(true);
		 }else if(numMarkers==75)
		 {
			 seventyFIve.setChecked(true);
		 }else{
			 hundres.setChecked(true);
		 }
		 if(allowDrunk==-1)
		 {
			 yes.setChecked(true);
		 }else{
			 no.setChecked(true);
		 }
		 
		Button update=(Button)findViewById(R.id.Update);
		update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int num=50;
				if(seventyFIve.isChecked())
				{
				num=75;	
				}else if(hundres.isChecked()){
					num=100;
				}
				int allow=-1;
				if(no.isChecked())
				{
					allow=1;
				}
				 SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
				   
				SharedPreferences.Editor editor = settings.edit();
			      editor.putInt("drunk", allow);
			      editor.putInt("numMarkers", num);
			      // Commit the edits!
			      editor.commit();
			}
		});
	}
}
