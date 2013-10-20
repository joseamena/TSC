package jose.antonio.mena.tonestackcalculator;

import java.text.DecimalFormat;
import java.util.ArrayList;

import jose.antonio.mena.tonestackcalculator.Circuit.CircuitType;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CircuitFragment extends Fragment implements OnSeekBarChangeListener {

	private ArrayList <SeekBar> mControls;
	private ArrayList <TextView> mControlLabels;
	private BodeView bodeView;
	private DecimalFormat df;
	private Circuit circuit;
	private Button mCircuitConfigButton;
	private float[] xRange;
	private float[] yRange;
	
	private final static String DIALOG_CIRCUIT = "circuit";
	public static final String EXTRA_CIRCUIT_ID =
			"jose.antonio.mena.tonestackcalculator.circuit_id";
	public static final String EXTRA_BODEVIEW_XRANGE = 
			"jose.antonio.mena.tonestackcalculator.bodeview_x_range";
	public static final String EXTRA_BODEVIEW_YRANGE = 
			"jose.antonio.mena.tonestackcalculator.bodeview_y_range";
	private final static String TAG = "Circuit Fragment";
	private final static int REQUEST_CODE = 0;			/*what is this for???*/
	
	private void updateCircuitBodeView() {
		bodeView.invalidate();
	}
	
	public CircuitFragment() {
		mControls = new ArrayList<SeekBar>();
		mControlLabels = new ArrayList<TextView>();	
		df = new DecimalFormat("0.00");
	}

	public static CircuitFragment newInstance (CircuitType type) {
		Bundle args = new Bundle();
		
		args.putSerializable(EXTRA_CIRCUIT_ID, type);
		CircuitFragment fragment = new CircuitFragment();
		fragment.setArguments(args);
		
		return fragment;
		
	}
	
	public static CircuitFragment newInstance (CircuitType type, float[] xrange, float[] yrange) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CIRCUIT_ID, type);
		args.putFloatArray(EXTRA_BODEVIEW_XRANGE, xrange);
		args.putFloatArray(EXTRA_BODEVIEW_YRANGE, yrange);
		
		CircuitFragment fragment = new CircuitFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CircuitType circuitType = (CircuitType) getArguments().getSerializable(EXTRA_CIRCUIT_ID);
		
		this.circuit = CircuitLab.get(getActivity()).getCircuit(circuitType);
		
		this.xRange = getArguments().getFloatArray(EXTRA_BODEVIEW_XRANGE);
		
		this.yRange = getArguments().getFloatArray(EXTRA_BODEVIEW_XRANGE);
						
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		View v = inflater.inflate(R.layout.fragment_tonestack, container, false);
		
		bodeView = (BodeView) v.findViewById(R.id.bodeView);
		
		bodeView.viewController = this;
		
		Log.v(TAG, "create instance with y range: " + yRange[0] + ", " + yRange[1]);
		
		Log.v(TAG, "create instance with x range: " + xRange[0] + ", " + xRange[1]);
		
		TableLayout controlsLayout = (TableLayout) v.findViewById(R.id.controlsView); 
		
		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
																	TableLayout.LayoutParams.WRAP_CONTENT);
	
		for (int i = 0; i < circuit.getNumberOfPotentiometers(); i++) {
			
			mControlLabels.add(new TextView(this.getActivity().getApplicationContext()));
			mControls.add(new SeekBar(this.getActivity().getApplicationContext()));
			mControls.get(i).setOnSeekBarChangeListener(this);
			TableRow row = new TableRow(this.getActivity().getApplicationContext());
			row.setGravity(Gravity.CENTER);
			double value = ((Potentiometer) circuit.getPotentiometers().get(i)).getPosition();
			
			mControlLabels.get(i).setTextColor(Color.BLACK);
			mControlLabels.get(i).setText(circuit.getPotentiometers().get(i).getName().
					concat(" ").concat(df.format(value)));
			mControlLabels.get(i).setEms(10);
			
			TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(0, 
					TableRow.LayoutParams.WRAP_CONTENT);
			textViewParams.weight = 0.25f;
			
			row.addView(mControlLabels.get(i), textViewParams);
			
			TableRow.LayoutParams sliderParams = new TableRow.LayoutParams(0,
					TableRow.LayoutParams.WRAP_CONTENT);
			sliderParams.weight = 1;
			mControls.get(i).setProgress((int) (value * 100));
			row.addView(mControls.get(i), sliderParams);
			
			((ViewGroup) controlsLayout).addView(row, tableParams);	
			
		}
		
		this.mCircuitConfigButton = (Button) v.findViewById(R.id.button1);
		this.mCircuitConfigButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				CircuitConfiguratorFragment dialog = CircuitConfiguratorFragment.newInstance(circuit);
				dialog.setTargetFragment(CircuitFragment.this, REQUEST_CODE);
				dialog.show(fm, DIALOG_CIRCUIT);	
			}
		});
		
		return v;
	}

	@Override
	public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
		int pos = mControls.indexOf(sb);
		
		double value = (double) (progress) / 100;
		Potentiometer potentiometer = (Potentiometer) circuit.getPotentiometers().get(pos);
		potentiometer.setPosition(value);
		bodeView.invalidate();
		mControlLabels.get(pos).setText(potentiometer.getName().concat(" ").
				concat(df.format(value)));
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public double [] getCurve(double[] points, boolean isLog, BodeView view) {
		
		return circuit.getCurve(points, isLog);	
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.v (TAG, "requestCode: " + requestCode + ", resultCode: " + resultCode + ", data: " + data);
		
		if (resultCode != Activity.RESULT_OK) {
			Log.v (TAG, "result not OK");
			return;
		}
		
		if (requestCode == CircuitFragment.REQUEST_CODE) {
			Bundle bundle = data.getBundleExtra(CircuitConfiguratorFragment.EXTRA_CIRCUIT);
			int size = bundle.getInt("size");
			Log.v (TAG, "size: " + size);
			this.updateCircuitBodeView();
		}
	}
	
}
