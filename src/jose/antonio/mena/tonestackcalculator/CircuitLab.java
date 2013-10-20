package jose.antonio.mena.tonestackcalculator;

import java.util.ArrayList;

import jose.antonio.mena.tonestackcalculator.Circuit.CircuitType;

import android.content.Context;

public class CircuitLab {
	private ArrayList<Circuit> circuits;
	
	private static CircuitLab sCircuitLab;
	private Context mAppContext;
	
	private CircuitLab (Context context) {
		mAppContext = context;
		circuits = new ArrayList<Circuit>();
		circuits.add(new BigMuffPiCircuit());
		circuits.add(new LowPassToneControl());
		
	}

	public static CircuitLab get (Context c) {
		if (sCircuitLab == null) {
			sCircuitLab = new CircuitLab(c.getApplicationContext());
		}
		return sCircuitLab;
	}
	
	public ArrayList<Circuit> getCircuits() {
		return circuits;
	}
	
	public Circuit getCircuitAt (int i) {
		if (circuits.size() > 0)
			return this.circuits.get(i);
		
		return null;
	}
	
	public Circuit getCircuit(Circuit c) {
		return this.circuits.get((this.circuits.indexOf(c)));
	}
	
	public Circuit getCircuit(CircuitType type) {
		for (int i = 0; i < circuits.size(); i++) {
			if (circuits.get(i).getType() == type)
				return circuits.get(i);
		}
		return null;
	}

}
