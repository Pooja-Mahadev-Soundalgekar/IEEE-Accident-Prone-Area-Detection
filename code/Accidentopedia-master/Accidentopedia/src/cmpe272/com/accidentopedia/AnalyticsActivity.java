package cmpe272.com.accidentopedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.LinearLayout;

public class AnalyticsActivity extends Activity {
Button showFilters=null,apply;
private HashMap<String,Integer> states=null,county=null,cities=null;
ArrayAdapter<String> statesadapter=null,countyadapter=null,citiesadapter=null;
LinearLayout ll;
ArrayList<String> stateList;
ArrayList<String> countyList,cityList;
Spinner state,counties,city,month,year;
int statecode=-1,citycode=-1,countycode=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analytics);
		states=new HashMap<String, Integer>();
		county=new HashMap<String, Integer>();
		cities=new HashMap<String, Integer>();
		stateList=new ArrayList<String>(states.keySet());
		countyList=new ArrayList<String>(county.keySet());
		cityList=new ArrayList<String>(cities.keySet());
		statesadapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown,stateList);
		countyadapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown,countyList);
		citiesadapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown,cityList);
		state=(Spinner)findViewById(R.id.state);
		state.setAdapter(statesadapter);
		counties=(Spinner)findViewById(R.id.county);
		counties.setAdapter(countyadapter);
		city=(Spinner)findViewById(R.id.city);
		city.setAdapter(citiesadapter);
		month=(Spinner)findViewById(R.id.month);
		year=(Spinner)findViewById(R.id.year);
		state.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
					statecode=states.get(state.getSelectedItem().toString());
					citycode=-1;
					cityList.clear();
					citiesadapter.notifyDataSetChanged();
					city.setSelected(false);
					counties.setSelected(false);
					countyList.clear();
					countyadapter.notifyDataSetChanged();
					countycode=-1;
					new MyTask((AppConstants.URL1+"/data/?p1="+statecode), county,countyList).execute(((Void) null));
					
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		counties.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				citycode=-1;
				if(cityList!=null)
				{cityList.clear();
				citiesadapter.notifyDataSetChanged();
				}
				city.setSelected(false);
				
				countycode=county.get(counties.getSelectedItem().toString());
				new MyTask((AppConstants.URL1+"/data/?p1="+statecode+"&p2="+countycode), cities,cityList).execute(((Void) null));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		city.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				citycode=cities.get(city.getSelectedItem());
				if(citycode!=-1)
				{
					month.setVisibility(View.GONE);
				}else{
					month.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		new MyTask((AppConstants.URL1+"/data"), states,stateList).execute(((Void) null));
		
		
		apply=(Button)findViewById(R.id.submit);
		apply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(AnalyticsActivity.this,TabsActivity.class);
				
					intent.putExtra("statecode", statecode);
				
				
					intent.putExtra("countycode", countycode);
				
					intent.putExtra("citycode", citycode);
				
				
					if(year.getSelectedItem().toString().equalsIgnoreCase("-Year-"))
					intent.putExtra("year", -1);
					else
						intent.putExtra("year", Integer.parseInt(year.getSelectedItem().toString()));
					if(month.getSelectedItem().toString().equalsIgnoreCase("-Month-"))
					intent.putExtra("month", -1);
					else{
						String ar[]=getResources().getStringArray(R.array.month);
						ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(ar));
						intent.putExtra("month",	arrayList.indexOf(month.getSelectedItem().toString())+1);
					}
				startActivity(intent);
			}
		});
		ll=(LinearLayout)findViewById(R.id.ll);
	}
	
	private class MyTask extends AsyncTask<Void, Void, Boolean> {
 String url;
HashMap<String,Integer> map;
ArrayList<String> list;
		public MyTask(String url,HashMap<String,Integer> map,ArrayList<String> list) {
	super();
	this.url = url;
	this.map=map;
	this.list=list;
	list.clear();
	map.clear();
}
		@Override
        protected void onPreExecute() {
           
        }
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			StringBuilder builder=new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpGet get=new HttpGet(url);
			 HttpResponse response;
			try {
				response = client.execute(get);
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	             String line = "";
	             while ((line = rd.readLine()) != null) {
	                 builder.append(line);
	             }
	             try {
					JSONArray ar=new JSONArray(builder.toString());
					if(ar.length()>1)
					{
						JSONArray items=ar.getJSONArray(0);
						map.put("ALL", -1);
						list.add("ALL");
						for(int i=0;i<items.length();i++)
						{
							JSONObject obj=items.getJSONObject(i);
							
							if(map==states)
							{
								//Log.d("Map", "states");
							map.put(obj.getString("sname"), Integer.parseInt(obj.getString("scode")));
							list.add(obj.getString("sname"));
							}else if(map==county){
								map.put(obj.getString("name"), Integer.parseInt(obj.getString("county")));
								list.add(obj.getString("name"));
								//Log.d("Map", "County");
							}else{
								map.put(obj.getString("name"), Integer.parseInt(obj.getString("city")));
								list.add(obj.getString("name"));
								//Log.d("Map", "cities");
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			

			if(map==states)
			{
				statesadapter.notifyDataSetChanged();
			}else if(map==county){
				countyadapter.notifyDataSetChanged();
				counties.setEnabled(true);
				city.setEnabled(false);
			}else{
				citiesadapter.notifyDataSetChanged();
				city.setEnabled(true);
			}
		}

		

		
}
	}
