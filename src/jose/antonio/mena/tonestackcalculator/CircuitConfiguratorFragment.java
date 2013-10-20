package jose.antonio.mena.tonestackcalculator;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CircuitConfiguratorFragment extends DialogFragment implements TextWatcher, OnFocusChangeListener{

	private Circuit mCircuit;
	//private ArrayList<View> componentViews;
	private CircuitComponent c;	//current component being edited
	
	public final static String EXTRA_CIRCUIT = "jose.antonio.mena.tonestackcalculator.circuit";
	private final static String TAG = "Circuit Configurator";
	
	private int getLayoutId(Circuit.CircuitType t) {
		int layoutId = 0;
		
		switch (t) {
		case BAXANDALL:
			break;
		case BIGMUFF:
			layoutId = R.layout.big_muff_settings;
			break;
		case LOWPASS:
			break;
		default:
			break;
		
		}
		return layoutId;
	}
	private void sendResult(int resultCode) {
		if (this.getTargetFragment() == null) {
			return;
		}
		Intent i = new Intent();
		Bundle b = new Bundle();
		//.putDoubleArray("Changed Components", new double[4]);
		b.putInt("size", mCircuit.getNumberOfComponents());
		
		String[] keys = new String[mCircuit.getNumberOfComponents()];
		
		for (int j = 0; j < mCircuit.getNumberOfComponents(); j++) {
			b.putDouble(mCircuit.mComponents.get(j).getName(), mCircuit.mComponents.get(j).getValue());
			keys[j] = mCircuit.mComponents.get(j).getName();
		}
		b.putStringArray("keys", keys);
		
		i.putExtra(EXTRA_CIRCUIT, b);
		
		this.getTargetFragment().onActivityResult(this.getTargetRequestCode(), resultCode, i);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = getActivity().getLayoutInflater().inflate(getLayoutId(mCircuit.getType()), null);
		int numChilds = ((ViewGroup) v).getChildCount();
		for (int i = 0; i < numChilds; i++) {
			//((TextView) ((ViewGroup) v).getChildAt(i)).getText();
			Log.v(TAG, ((ViewGroup) v).getChildAt(i).getClass().getName());
			View tempView = ((ViewGroup) v).getChildAt(i);
			if (tempView.getClass() == TextView.class || tempView.getClass() == EditText.class) {
				Log.v(TAG, "class is TextView lets get the Tag");
				if (tempView.getTag() != null) {
					Log.v(TAG, "The tag is " + tempView.getTag());
					
					CircuitComponent c = mCircuit.getCircuitComponentByName((String) tempView.getTag());
					if (c != null) {
						tempView.setTag(R.id.component, c);
					}
					
					if(tempView.getClass() == EditText.class) {
						((EditText) tempView).setText(c.getStringValue());
						((EditText) tempView).addTextChangedListener(this);
						((EditText) tempView).setOnFocusChangeListener(this);
					} else {
						((TextView) tempView).setText(c.getName());
					}
							
				} else {
					Log.v(TAG, "TAG was null");
				}
				
			}
		}
		/*
		ArrayList<CircuitComponent> components = mCircuit.mComponents;
		for (int i = 0; i < components.size(); i++){
			Log.v(TAG, components.get(i).getName().toLowerCase()+"-value");
		}
			
			TableRow row = new TableRow(getActivity());
			
			TextView label = new TextView(getActivity());
			label.setText(components.get(i).getName()+ ": ");
			label.setTag(components.get(i));
			
			EditText editValue = new EditText(getActivity());
			editValue.setText(components.get(i).getStringValue());
			editValue.addTextChangedListener(this);
			editValue.setOnFocusChangeListener(this);
			editValue.setTag(components.get(i));
			editValue.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
			
			row.addView(label);
			row.addView(editValue);
			
			((TableLayout) v).addView(row);
		}*/
		return new AlertDialog
				.Builder(getActivity())
				.setView(v)
				.setTitle(this.mCircuit.getTitle())
				.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sendResult(Activity.RESULT_OK);
					}
				})
				.create();
		
	}
	
	public static CircuitConfiguratorFragment newInstance(Circuit circuit) {	
		CircuitConfiguratorFragment fragment = new CircuitConfiguratorFragment();
		fragment.mCircuit = circuit;
		return fragment;
		
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		Log.v (TAG,"afterTextChanged");

		double[] rValue = new double[1];
		if (arg0.length() > 0) {
			Log.v (TAG, "value before parsing: " + rValue[0]);
			if (CircuitComponent.parseValue(arg0.toString(), rValue)) {
				if (c != null) {
					c.setValue(rValue[0]);
				}
			} 
			Log.v (TAG, "value after parsing: " + rValue[0]);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
		Log.v (TAG, "beforeTextChanged");
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		Log.v (TAG,"onTextChanged");
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		CircuitComponent temp = (CircuitComponent) ((View) v).getTag(R.id.component);
		if (!hasFocus) {
			Log.v(TAG, "component " + temp.getName() + " changed");
		}
		else {
			Log.v(TAG, "component " + temp.getName() + " has focus");
			this.c = temp;
		}		
	}
	

}
