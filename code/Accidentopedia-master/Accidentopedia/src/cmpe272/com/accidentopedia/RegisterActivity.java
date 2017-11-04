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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
EditText fname,lname,email,password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		fname=(EditText)findViewById(R.id.fname);
		lname=(EditText)findViewById(R.id.lname);
		email=(EditText)findViewById(R.id.email);
		password=(EditText)findViewById(R.id.password);
		Button regi=(Button)findViewById(R.id.register);
		regi.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				attemptRegisteration();
				
			}
		});
	}
	private void attemptRegisteration()
	{
		new UserLoginTask(email.getText().toString(), password.getText().toString(), fname.getText().toString(), lname.getText().toString()).execute((Void) null);
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

       String mEmail,fname,lastname;
         String mPassword;

        UserLoginTask(String ema, String pass,String fname,String lastname) {
            this.mEmail = ema;
            this.mPassword = pass;
           this.fname=fname;
          this.lastname=lastname;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(AppConstants.URL1+"/register");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("username",
                        mEmail));
               // Log.d("Response", mEmail+"\t"+mPassword);
                nameValuePairs.add(new BasicNameValuePair("password",
                        mPassword));
                nameValuePairs.add(new BasicNameValuePair("firstname",
                        fname));
                nameValuePairs.add(new BasicNameValuePair("lastname",
                       lastname));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(post);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    builder.append(line);
                }
                Log.d("Response", builder.toString());
                
                    return true;
                
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
