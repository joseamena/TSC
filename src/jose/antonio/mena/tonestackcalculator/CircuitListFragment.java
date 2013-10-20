package jose.antonio.mena.tonestackcalculator;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CircuitListFragment extends ListFragment {
	
	private ArrayList<Circuit> mCircuits;
	
	private static final String TAG = "CircuitListFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.v (TAG, "onCreate");
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		//getActivity().setTitle(R.string.hello_world);
		
		mCircuits = CircuitLab.get(getActivity()).getCircuits();
		
		ArrayAdapter<Circuit> adapter = new ArrayAdapter<Circuit> (getActivity(), 
							android.R.layout.simple_list_item_1, mCircuits);
	
		setListAdapter(adapter);
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		Circuit c = (Circuit) this.getListAdapter().getItem(position);
		
		Log.v (TAG, c.getTitle() + " was clicked");
		
		Intent i = new Intent(getActivity(), CircuitActivity.class);
		
		//ad an extra to the intent so that the circuit activity knows
		//which circuit fragment to create
		
		i.putExtra(CircuitFragment.EXTRA_CIRCUIT_ID, c.getType());
		
		//add the range as an extra
		float yrange[] = new float[2];
		yrange[0] = -35;
		yrange[1] = 0;
		
		i.putExtra(CircuitFragment.EXTRA_BODEVIEW_YRANGE, yrange);
		
		float xrange[] = new float[2];
		xrange[0] = 20;
		xrange[1] = 20000;
		
		i.putExtra(CircuitFragment.EXTRA_BODEVIEW_XRANGE, xrange);
		
		startActivity(i);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_circuit_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_settings:
				Log.v (TAG, "show the preferences screen");
				Intent i = new Intent(getActivity(), PreferencesActivity.class);
				startActivity(i);
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
				
		}
		
		
	}
	
	
	
	

}
