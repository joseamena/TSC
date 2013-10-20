package jose.antonio.mena.tonestackcalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PreferencesFragment extends Fragment {
	
	private Spinner mHorizontalAxisSelector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		 
		View v = inflater.inflate(R.layout.general_preferences, container, false);
		
		mHorizontalAxisSelector = (Spinner) v.findViewById(R.id.horizontal_axis_selector);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), 
												R.array.horizontal_axis_options_array, 
												android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		mHorizontalAxisSelector.setAdapter(adapter);
		
		
		return v;
		
		
	}
	
	

}
