package cmpe272.com.accidentopedia;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
Button register,login;
EditText email,password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		register=(Button)findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
				
			}
		});
		login=(Button)findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				attemptLogin();
				 Log.d(email.getText().toString(),password.getText().toString());
			}
		});
		email=(EditText)findViewById(R.id.username);
		password=(EditText)findViewById(R.id.password);
	}
	private void attemptLogin()
	{
		email.setError(null);
		password.setError(null);
		if(!isEmailValid(email.getText().toString()))
		{
			email.setError("Not a valid email address");
		}else if(!isPasswordValid(password.getText().toString()))
		{
			password.setError("Not a valid email address");
		}else{
			new UserLoginTask(email.getText().toString(), password.getText().toString()).execute((Void) null);
		}
	}
	 private boolean isEmailValid(String email) {
	        //TODO: Replace this with your own logic
	        return email.contains("@");
	    }

	    private boolean isPasswordValid(String password) {
	        //TODO: Replace this with your own logic
	        return password.length() > 4;
	    }
	    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

	        private final String mEmail;
	        private final String mPassword;

	        UserLoginTask(String ema, String pass) {
	            mEmail = ema;
	            mPassword = pass;
	           
	        }

	        @Override
	        protected Boolean doInBackground(Void... params) {
	            // TODO: attempt authentication against a network service.
	            StringBuilder builder = new StringBuilder();
	            HttpClient client = new DefaultHttpClient();
	            HttpPost post = new HttpPost(AppConstants.URL1+"/login");
	            try {
	                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	                nameValuePairs.add(new BasicNameValuePair("username",
	                        mEmail));
	                Log.d("Response", mEmail+"\t"+mPassword);
	                nameValuePairs.add(new BasicNameValuePair("password",
	                        mPassword));
	                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	                HttpResponse response = client.execute(post);
	                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	                String line = "";
	                while ((line = rd.readLine()) != null) {
	                    builder.append(line);
	                }
	                
	                JSONArray ar = new JSONArray(builder.toString());
	                if(ar.length()>0)
	                {
	                	JSONObject obj=(JSONObject) ar.get(0);
	                
	                    SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
	                    SharedPreferences.Editor editor = settings.edit();
	                    editor.putInt("id", obj.getInt("userid"));
	                    
	                    // Commit the edits!
	                    editor.commit();
	                    Log.d("Response", settings.getInt("id", -1)+"");
	                    return true;
	                
	                }else{
	                	return false;
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }


	            // TODO: register the new account here.
	            return false;
	        }

	        @Override
	        protected void onPostExecute(final Boolean success) {
	            
	            if (success) {
	                finish();
	            } else {
	                password.setError("Invalid Email ID/Password");
	                
	            }
	        }

	       
	    }
}
