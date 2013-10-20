package jose.antonio.mena.tonestackcalculator;

import jose.antonio.mena.tonestackcalculator.Circuit.CircuitType;
import android.support.v4.app.Fragment;
import android.util.Log;

public class CircuitActivity extends SingleFragmentActivity {

	private final static String TAG = "Circuit Activity"; 

	@Override
	protected Fragment createFragment() {
		
		CircuitType circuitType = (CircuitType) getIntent().
				getSerializableExtra(CircuitFragment.EXTRA_CIRCUIT_ID);
		
		Log.v(TAG, "circuit Type: " + circuitType);
		
		float yrange[] = getIntent().getFloatArrayExtra(CircuitFragment.EXTRA_BODEVIEW_YRANGE);
		float xrange[] = getIntent().getFloatArrayExtra(CircuitFragment.EXTRA_BODEVIEW_XRANGE);
		
		Log.v(TAG, "create instance with y range: " + yrange[0] + ", " + yrange[1]);
		
		Log.v(TAG, "create instance with x range: " + xrange[0] + ", " + xrange[1]);
		
		return CircuitFragment.newInstance(circuitType, xrange, yrange);
		
	}
}
