package jose.antonio.mena.tonestackcalculator;

import android.support.v4.app.Fragment;
import android.util.Log;

public class CircuitListActivity extends SingleFragmentActivity {

	private final static String TAG = "CircuitListActivity";
	@Override
	protected Fragment createFragment() {
		Log.v (TAG, "creating fragment");
		return new CircuitListFragment();
	}

}
