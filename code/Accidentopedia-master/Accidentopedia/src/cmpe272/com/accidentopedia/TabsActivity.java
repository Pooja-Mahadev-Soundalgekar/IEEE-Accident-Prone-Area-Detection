package cmpe272.com.accidentopedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;


import com.google.android.gms.maps.model.LatLng;

public class TabsActivity extends TabActivity {
	TabHost.TabSpec tab2;
	TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);

		int statecode = getIntent().getIntExtra("statecode", -1);
		int countycode = getIntent().getIntExtra("countycode", -1);
		int citycode = getIntent().getIntExtra("citycode", -1);
		int year = getIntent().getIntExtra("year", -1);
		int month = getIntent().getIntExtra("month", -1);
		Log.d("codes", statecode + "," + citycode + "," + countycode + ","
				+ year + "," + month);
		TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
	tab2 = tabHost.newTabSpec("Second Tab");
		TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");
		if (citycode == -1) {
			tab1.setIndicator("Bar Chart");
			tab2.setIndicator("Histogram");
			tab3.setIndicator("Pie chart");

			String barchart = "", histogram = "", piechart = "";
			if (statecode == -1 && year == -1 && month == -1) {

				// Set the Tab name and Activity
				// that will be opened when particular Tab will be selected
				barchart = "/states/barchart";
				histogram = "/states/histogram";

				piechart = "/states/piechart";

				/** Add the tabs to the TabHost to display. */

			} else if (statecode == -1 && month == -1) {

				// Set the Tab name and Activity
				// that will be opened when particular Tab will be selected

				barchart = "/states/barchart" + year;

				histogram = "/states/histogram/" + year;

				piechart = "/states/piechart/" + year;

			} else if (statecode == -1) {
				// Set the Tab name and Activity
				// that will be opened when particular Tab will be selected
				barchart = "/states/barchart/" + year + "/" + month;
				histogram = "/states/histogram/" + year + "/" + month;
				piechart = "/states/piechart/" + year + "/" + month;

			} else if (countycode == -1 && year == -1 && month == -1) {

				// Set the Tab name and Activity
				// that will be opened when particular Tab will be selected
				barchart = "/county/barchart/" + statecode;

				histogram = "/county/histogram/" + statecode;

				piechart = "/county/piechart/" + statecode;

				/** Add the tabs to the TabHost to display. */

			} else if (countycode == -1 && statecode != -1 && month == -1) {

				// Set the Tab name and Activity
				// that will be opened when particular Tab will be selected
				barchart = "/county/barchart/" + statecode + "/" + year;

				histogram = "/county/histogram/" + statecode + "/" + year;

				piechart = "/county/piechart/" + statecode + "/" + year;

			} else if (statecode != -1 && countycode == -1) {

				// Set the Tab name and Activity
				// that will be opened when particular Tab will be selected
				barchart = "/county/barchart/" + statecode + "/" + year + "/"
						+ month;

				histogram = "/county/histogram/" + statecode + "/" + year + "/"
						+ month;

				piechart = "/county/piechart/" + statecode + "/" + year + "/"
						+ month;

				/** Add the tabs to the TabHost to display. */

			} else if (citycode == -1 && year == -1 && month == -1) {

				// Set the Tab name and Activity
				// that will be opened when particular Tab will be selected
				barchart = "/city/barchart/" + countycode;

				histogram = "/city/histogram/" + countycode;

				piechart = "/city/piechart/" + countycode;

				/** Add the tabs to the TabHost to display. */

			} else if (citycode == -1 && countycode != -1 && month == -1) {

				// Set the Tab name and Activity
				// that will be opened when particular Tab will be selected
				barchart = "/city/barchart/" + countycode + "/" + year;

				histogram = "/city/histogram/" + countycode + "/" + year;

				piechart = "/city/piechart/" + countycode + "/" + year;

			} else if (countycode != -1 && citycode == -1) {

				// Set the Tab name and Activity
				// that will be opened when particular Tab will be selected
				barchart = "/city/barchart/" + countycode + "/" + year + "/"
						+ month;

				histogram = "/city/histogram/" + countycode + "/" + year + "/"
						+ month;

				piechart = "/city/piechart/" + countycode + "/" + year + "/"
						+ month;

				/** Add the tabs to the TabHost to display. */

			}
			Intent bar = new Intent(this, ChartActivity.class);
			bar.putExtra("URL", barchart);
			tab1.setIndicator("Bar Chart");
			tab1.setContent(bar);
			Intent histo = new Intent(this, ChartActivity.class);
			histo.putExtra("URL", histogram);
			tab2.setContent(histo);
			Intent pie = new Intent(this, ChartActivity.class);
			pie.putExtra("URL", piechart);
			tab3.setIndicator("Pie chart");
			tab3.setContent(pie);
			tabHost.addTab(tab1);
			tabHost.addTab(tab2);
			tabHost.addTab(tab3);
		} else {
			String barchart="/city/drunk/"+citycode;
			if (year != -1) {
				barchart=barchart+"/"+year;
			} 
			
			Intent bar = new Intent(this, ChartActivity.class);
			bar.putExtra("URL", barchart);
			tab1.setIndicator("Drunk Stats");
			tab1.setContent(bar);
			tabHost.addTab(tab1);
			try {
				SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
			       int allowDrunk = settings.getInt("drunk", -1);
			       int numMarkers=settings.getInt("numMarkers", 50);
			JSONObject obj=new JSONObject();
			obj.put("statecode", statecode);
			obj.put("countycode", countycode);
			obj.put("city", citycode);
			obj.put("ddr", allowDrunk);
		
				obj.put("limit", numMarkers);
				new MyTask((AppConstants.URL1+"/accident" ), obj).execute(((Void) null));
				
				Log.d("JSON", obj.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
///accident 
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	private class MyTask extends AsyncTask<Void, Void, Boolean> {
		 String url;JSONObject obj;
		
		ArrayList<LatLng> list;
				public MyTask(String url,JSONObject obj) {
			super();
			this.url = url;
			list=new ArrayList<LatLng>();
			this.obj=obj;
		}
				@Override
		        protected void onPreExecute() {
		           
		        }
				@Override
				protected Boolean doInBackground(Void... params) {
					// TODO Auto-generated method stub
					try {
					StringBuilder builder=new StringBuilder();
					HttpClient client = new DefaultHttpClient();
					HttpPost get=new HttpPost(url);
					StringEntity  postingString =new StringEntity(obj.toString());//convert your pojo to   json
					get.setEntity(postingString);
					 HttpResponse response;
					
						response = client.execute(get);
						BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			             String line = "";
			             while ((line = rd.readLine()) != null) {
			                 builder.append(line);
			             }
			             try {
							JSONArray ar=(JSONArray) new JSONArray(builder.toString()).get(0);
							if(ar.length()>0)
							{
								for(int i=0;i<ar.length();i++)
								{
									JSONObject obj=ar.getJSONObject(i);
									String point=obj.getString("astext(location)");
									Log.d("point", point);
					                
									point=point.replace("POINT", "");
			                		point=point.substring(1,point.length()-1);
			                		LatLng ln=new LatLng(Double.parseDouble(point.split(" ")[0]), Double.parseDouble(point.split(" ")[1]));
			                		list.add(ln);
			                		//Log.d("point", Double.parseDouble(point.split(" ")[1])+"\t"+ Double.parseDouble(point.split(" ")[0]));
			                	//	
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
					Intent bar = new Intent(TabsActivity.this, MapActivity.class);
					//(ArrayList<LatLng>) getIntent().getSerializableExtra("points")
					bar.putExtra("points", list);
					tab2.setIndicator("Map View");
					tab2.setContent(bar);
					tabHost.addTab(tab2);

					
				}

				

				
		}
}
