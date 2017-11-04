package cmpe272.com.accidentopedia;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link Leftpane.OnFragmentInteractionListener} interface
 * to handle interaction events. Use the {@link Leftpane#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class Leftpane extends android.support.v4.app.Fragment {
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.leftpane, container, true);
   
        return v;
	}



}
